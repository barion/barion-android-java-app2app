/**
 * Copyright 2015 Barion Payment Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.barion.example.app2app.nativeintegration.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barion.example.app2app.nativeintegration.Preferences;
import com.barion.example.app2app.nativeintegration.activities.PaymentResultActivity;
import com.barion.example.app2app.nativeintegration.activities.WebViewActivity;
import com.barion.example.app2app.nativeintegration.gui.ProductAdapter;
import com.barion.example.app2app.nativeintegration.models.BarionError;
import com.barion.example.app2app.nativeintegration.models.BarionStartPaymentResponse;
import com.barion.example.app2app.nativeintegration.models.Product;
import com.barion.example.app2app.nativeintegration.R;
import com.barion.example.app2app.nativeintegration.utils.Globals;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShopFragment extends ListFragment implements View.OnClickListener {

    private int PAYMENT_REQUEST_CODE = 10;

    private String paymentId;

    private ArrayList<Product> products;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        products = Product.getProducts();

        setListAdapter(new ProductAdapter(getActivity(), products));

        v.findViewById(R.id.payButton).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payButton:
                if(getActivity() != null) {
                    progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.preparingpayment), true, false);
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost post = new HttpPost(Preferences.URL_STARTPAYMENT);
                            try {

                                JSONArray productsArray = new JSONArray();
                                for(int i = 0; i < products.size(); ++i) {
                                    Product p = products.get(i);
                                    JSONObject productJson = new JSONObject();
                                    productJson
                                            .put("Name", p.getName())
                                            .put("Description", p.getDescription())
                                            .put("Quantity", p.getQuantity())
                                            .put("Unit", p.getUnit())
                                            .put("UnitPrice", p.getPrice())
                                            .put("ItemTotal", p.getPrice())
                                            .put("SKU", p.getSKU());
                                    productsArray.put(productJson);
                                }

                                JSONObject holderJson = new JSONObject();
                                holderJson.put("Products", productsArray);

                                post.setHeader(HTTP.CONTENT_TYPE,
                                        "application/json;charset=UTF-8");
                                post.setEntity(new StringEntity(holderJson.toString(), "UTF-8"));


                                HttpResponse response = httpClient.execute(post);
                                InputStream stream = response.getEntity().getContent();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                                StringBuilder builder = new StringBuilder();

                                String line;

                                while((line = reader.readLine()) != null) {
                                    builder.append(line);
                                }

                                return builder.toString();
                            } catch (Exception x) {
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if(s == null) networkError();
                            try {
                                handler.sendEmptyMessage(0);
                                JSONObject resultObject = new JSONObject(s);
                                BarionStartPaymentResponse response = BarionStartPaymentResponse.fromJsonObject(resultObject);
                                if(response.getErrors().size() != 0) processBarionErrors(response.getErrors());
                                else navigateUserToBarion(response);
                            } catch (JSONException e) {
                                networkError();
                            }

                        }
                    }.execute();
                }
                break;
        }
    }

    private void networkError() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() == null) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.networkerror));
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        });
    }

    private void processBarionErrors(final ArrayList<BarionError> errors) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() == null) return;
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < errors.size(); ++i) {
                   builder.append(errors.get(i).getDescription()).append("\n");
                }
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setMessage(builder.toString());
                alertBuilder.setPositiveButton(android.R.string.ok, null);
                alertBuilder.show();
            }
        });
    }

    private void navigateUserToBarion(final BarionStartPaymentResponse response) {
        if(getActivity() == null) return;
        Intent hasBarion = getActivity().getPackageManager().getLaunchIntentForPackage("barion.app");
        if(hasBarion != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            paymentId = response.getPaymentId();
            intent.setData(Uri.parse("barion:?pid=" + paymentId + "&action=returntoapp"));
            startActivityForResult(intent, PAYMENT_REQUEST_CODE);
        } else {
            Intent webIntent = new Intent(getActivity(), WebViewActivity.class);
            webIntent.putExtra(Globals.KEY_STARTRESPONSE, response);
            startActivity(webIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYMENT_REQUEST_CODE) {
            if(getActivity() != null) {
                Intent intent = new Intent(getActivity(), PaymentResultActivity.class);
                intent.putExtra(Globals.KEY_PAYMENTID, paymentId);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}

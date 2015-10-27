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
package com.barion.example.app2app.nativeintegration.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.barion.example.app2app.nativeintegration.Preferences;
import com.barion.example.app2app.nativeintegration.R;
import com.barion.example.app2app.nativeintegration.models.BarionError;
import com.barion.example.app2app.nativeintegration.models.BarionGetPaymentStateResponse;
import com.barion.example.app2app.nativeintegration.models.PaymentState;
import com.barion.example.app2app.nativeintegration.utils.Globals;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PaymentResultActivity extends ActionBarActivity {

    private TextView titleTextView;
    private TextView detailTextView;

    private ProgressDialog progressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.activity_title);

        actionBar.setLogo(getResources().getDrawable(R.drawable.icontext));

        titleTextView = (TextView) findViewById(R.id.resulttitle);
        detailTextView = (TextView) findViewById(R.id.resultdetail);

        Intent intent = getIntent();
        final String paymentId;
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            paymentId = intent.getDataString().replace(Preferences.REDIRECT_URL + "?paymentId=", "");
        } else {
            paymentId = intent.getStringExtra(Globals.KEY_PAYMENTID);
        }

        progressDialog = ProgressDialog.show(this, null, getString(R.string.loading), true, false);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String urlString = Preferences.URL_GETPAYMENTSTATE +
                            "?PaymentId=" + paymentId;
                    URL url = new URL(urlString);
                    URLConnection connection = url.openConnection();
                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    return builder.toString();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s == null) networkError();
                else {
                    try {
                        JSONObject object = new JSONObject(s);
                        BarionGetPaymentStateResponse response = BarionGetPaymentStateResponse.fromJsonObject(object);
                        if (response.getErrors().size() != 0)
                            processBarionErrors(response.getErrors());
                        else processPaymentState(response.getPaymentState());
                    } catch (JSONException e) {
                        networkError();
                    }
                }
            }
        }.execute(paymentId);
    }

    private void networkError() {
        handler.sendEmptyMessage(0);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentResultActivity.this);
                builder.setMessage(getString(R.string.networkerror));
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        });
    }

    private void processBarionErrors(final ArrayList<BarionError> errors) {
        handler.sendEmptyMessage(0);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                titleTextView.setText(R.string.result_fail_title);
                detailTextView.setText(errors.get(0).getDescription());
            }
        });
    }

    private void processPaymentState(final PaymentState state) {
        handler.sendEmptyMessage(0);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String status = state.getStatus();
                int detailId = -1;
                titleTextView.setText(getString(R.string.stateofpayment) + " " + status);
                switch (status.toLowerCase()) {
                    case "prepared" : detailId = R.string.detail_prepared; break;
                    case "started" : detailId = R.string.detail_started; break;
                    case "reserved" : detailId = R.string.detail_reserved; break;
                    case "canceled" : detailId = R.string.detail_canceled; break;
                    case "succeeded" : detailId = R.string.detail_succeeded; break;
                    case "failed" : detailId = R.string.detail_failed; break;
                    default: detailId = R.string.unknown; break;
                }
                detailTextView.setText(getString(detailId));
            }
        });
    }
}

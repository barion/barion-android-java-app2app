/**
 * Copyright 2015 Barion Payment Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.barion.example.app2app.libraryintegration.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barion.example.app2app.libraryintegration.Preferences;
import com.barion.example.app2app.libraryintegration.R;
import com.barion.example.app2app.libraryintegration.activities.PaymentResultActivity;
import com.barion.example.app2app.libraryintegration.gui.ProductAdapter;
import com.barion.example.app2app.libraryintegration.utils.Globals;

import java.util.ArrayList;

import barion.com.barionlibrary.activities.BarionActivity;
import barion.com.barionlibrary.models.BarionError;
import barion.com.barionlibrary.models.BarionGetPaymentStateResponse;
import barion.com.barionlibrary.models.BarionProduct;
import barion.com.barionlibrary.models.PaymentSettingsModel;
import barion.com.barionlibrary.models.TransactionModel;
import barion.com.barionlibrary.utils.Barion;

public class ShopFragment extends ListFragment implements View.OnClickListener {

    private static final String TAG = ShopFragment.class.getName();

    private ArrayList<BarionProduct> products;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        products = getProducts();

        setListAdapter(new ProductAdapter(getActivity(), products));

        v.findViewById(R.id.payButton).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payButton:
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), BarionActivity.class);

                    intent.putExtra(Barion.PRODUCTS, products);
                    intent.putExtra(Barion.PAYMENT_SETTINGS, new PaymentSettingsModel.Builder()
                            .posKey(Preferences.POSKEY)
                            .paymentType(PaymentSettingsModel.PaymentTypeEnum.Immediate)
                            .guestCheckOut(true)
                            .debugMode(true)
                            .locale("hu-HU")
                            .fundingSources(PaymentSettingsModel.FundingSourceTypeEnum.All)
                            .redirectUrl(Preferences.REDIRECT_URL).build());

                    intent.putExtra(Barion.TRANSACTION_SETTINGS, new TransactionModel.Builder()
                            .posTransactionId(Preferences.TRANSACTION_ID)
                            .payee(Preferences.PAYEE)
                            .total(200)
                            .comment("The new App2App Library").build());

                    startActivityForResult(intent, Barion.REQUEST_CODE);
                }
                break;
        }
    }

    private void processBarionErrors(final ArrayList<BarionError> errors) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) return;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < errors.size(); ++i) {
                    builder.append(errors.get(i).getDescription()).append("\n");
                }
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setMessage(builder.toString());
                alertBuilder.setPositiveButton(android.R.string.ok, null);
                alertBuilder.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Barion.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                BarionGetPaymentStateResponse response = (BarionGetPaymentStateResponse) data.getSerializableExtra(Barion.BARION_PAYMENT_STATE_RESPONSE);
                if (response != null && getActivity() != null) {
                    Intent intent = new Intent(getActivity(), PaymentResultActivity.class);
                    intent.putExtra(Globals.KEY_PAYMENTSTATE, response);
                    startActivity(intent);
                    getActivity().finish();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data.hasExtra(Barion.ERROR)) {
                    ArrayList<BarionError> errors = (ArrayList<BarionError>) data.getSerializableExtra(Barion.ERROR);
                    processBarionErrors(errors);
                } else if (data.hasExtra(Barion.BARION_PAYMENT_STATE_RESPONSE)) {
                    BarionGetPaymentStateResponse response = (BarionGetPaymentStateResponse) data.getSerializableExtra(Barion.BARION_PAYMENT_STATE_RESPONSE);
                    if (response != null) {
                        Log.d(TAG, "Response: " + response.getPaymentState().getStatus());
                    }
                }
            }
        }
    }

    private ArrayList<BarionProduct> getProducts() {
        return new ArrayList<BarionProduct>() {{
            add(new BarionProduct("The history of money", "The evolution of money from the stoneage to Barion", 1f, "db", 40, 40, "APP2APPDEMO_PENZTORT"));
            add(new BarionProduct("How to make a fortune with Barion", "Tips to get rich", 1f, "db", 20, 20, "APP2APPDEMO_SOKPENZ"));
            add(new BarionProduct("The begginers guide to accepting bank cards", "How to become bank card acceptors in five minutes with Barion", 1f, "db", 140, 140, "APP2APPDEMO_BANKKARI"));
        }};
    }
}

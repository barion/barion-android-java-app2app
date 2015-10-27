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
package com.barion.example.app2app.libraryintegration.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.barion.example.app2app.libraryintegration.R;
import com.barion.example.app2app.libraryintegration.utils.Globals;

import java.util.ArrayList;

import barion.com.barionlibrary.models.BarionError;
import barion.com.barionlibrary.models.BarionGetPaymentStateResponse;
import barion.com.barionlibrary.models.PaymentState;

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
        BarionGetPaymentStateResponse startPaymentResponse = (BarionGetPaymentStateResponse) intent.getSerializableExtra(Globals.KEY_PAYMENTSTATE);
        if (startPaymentResponse.getErrors() != null && startPaymentResponse.getErrors().size() > 0) {
            processBarionErrors(startPaymentResponse.getErrors());
        } else {
            processPaymentState(startPaymentResponse.getPaymentState());
        }
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
                    case "prepared":
                        detailId = R.string.detail_prepared;
                        break;
                    case "started":
                        detailId = R.string.detail_started;
                        break;
                    case "reserved":
                        detailId = R.string.detail_reserved;
                        break;
                    case "canceled":
                        detailId = R.string.detail_canceled;
                        break;
                    case "succeeded":
                        detailId = R.string.detail_succeeded;
                        break;
                    case "failed":
                        detailId = R.string.detail_failed;
                        break;
                    default:
                        detailId = R.string.unknown;
                        break;
                }
                detailTextView.setText(getString(detailId));
            }
        });
    }
}

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
package com.barion.example.app2app.nativeintegration.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class BarionStartPaymentResponse implements Serializable {

    private String paymentId;
    private String paymentRequestId;
    private String status;
    private ArrayList<PaymentTransaction> paymentTransactions;
    private ArrayList<BarionError> errors;

    private BarionStartPaymentResponse(String paymentId, String paymentRequestId, String status, ArrayList<PaymentTransaction> paymentTransactions, ArrayList<BarionError> errors) {
        this.paymentId = paymentId;
        this.paymentRequestId = paymentRequestId;
        this.status = status;
        this.paymentTransactions = paymentTransactions;
        this.errors = errors;
    }

    public static BarionStartPaymentResponse fromJsonObject(JSONObject object) {
        if (object == null) return null;

        String paymentId = object.optString("PaymentId");
        String paymentRequestId = object.optString("PaymentRequestId");
        String status = object.optString("Status");
        ArrayList<PaymentTransaction> paymentTransactions = new ArrayList<>();
        JSONArray transactionsArray = object.optJSONArray("Transactions");
        if (transactionsArray != null) {
            for (int i = 0; i < transactionsArray.length(); ++i) {
                try {
                    JSONObject transactionObject = transactionsArray.getJSONObject(i);
                    PaymentTransaction t = PaymentTransaction.fromJsonObject(transactionObject);
                    if (t != null) paymentTransactions.add(t);
                } catch (JSONException e) {
                }
            }
        }

        ArrayList<BarionError> errors = new ArrayList<>();
        JSONArray errorsArray = object.optJSONArray("Errors");
        if (errorsArray != null) {
            for (int i = 0; i < errorsArray.length(); ++i) {
                try {
                    JSONObject errorObject = errorsArray.getJSONObject(i);
                    BarionError error = BarionError.fromJsonObject(errorObject);
                    if (error != null) errors.add(error);
                } catch (JSONException e) {
                }
            }
        }

        return new BarionStartPaymentResponse(paymentId, paymentRequestId, status, paymentTransactions, errors);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public ArrayList<BarionError> getErrors() {
        return errors;
    }
}

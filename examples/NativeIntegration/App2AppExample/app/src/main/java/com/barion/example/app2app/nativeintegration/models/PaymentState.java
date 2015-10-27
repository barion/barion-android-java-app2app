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

import com.barion.example.app2app.nativeintegration.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PaymentState implements Serializable {

    private String paymentId;
    private String paymentRequestId;
    private String posId;
    private String posName;
    private String status;
    private String paymentType;
    private String fundingSource;
    private boolean guestCheckout;
    private Date createdAt;
    private Date validUntil;
    private Date completedAt;
    private Date reservedUntil;
    private ArrayList<Transaction> transactions;

    private PaymentState(String paymentId, String paymentRequestId, String posId, String posName, String status, String paymentType, String fundingSource, boolean guestCheckout, Date createdAt, Date validUntil, Date completedAt, Date reservedUntil, ArrayList<Transaction> transactions) {
        this.paymentId = paymentId;
        this.paymentRequestId = paymentRequestId;
        this.posId = posId;
        this.posName = posName;
        this.status = status;
        this.paymentType = paymentType;
        this.fundingSource = fundingSource;
        this.guestCheckout = guestCheckout;
        this.createdAt = createdAt;
        this.validUntil = validUntil;
        this.completedAt = completedAt;
        this.reservedUntil = reservedUntil;
        this.transactions = transactions;
    }

    public static PaymentState fromJsonObject(JSONObject object) {
        if (object == null) return null;

        String paymentId = object.optString("PaymentId");
        String paymentRequestId = object.optString("PaymentRequestId");
        String posId = object.optString("POSId");
        String posName = object.optString("POSName");
        String status = object.optString("Status");
        String paymentType = object.optString("PaymentType");
        String fundingSource = object.optString("FundingSource");
        boolean guestCheckout = object.optBoolean("GuestCheckout");
        Date createdAt = Utils.parseJsonDate(object, "CreatedAt");
        Date validUntil = Utils.parseJsonDate(object, "ValidUntil");
        Date completedAt = Utils.parseJsonDate(object, "CompletedAt");
        Date reservedUntil = Utils.parseJsonDate(object, "ReservedUntil");
        ArrayList<Transaction> transactions = new ArrayList<>();
        JSONArray transactionArray = object.optJSONArray("Transactions");
        if (transactionArray != null) {
            for (int i = 0; i < transactionArray.length(); ++i) {
                JSONObject transactionObject = transactionArray.optJSONObject(i);
                Transaction t = Transaction.fromJsonObject(transactionObject);
                if (t != null) transactions.add(t);
            }
        }
        return new PaymentState(paymentId, paymentRequestId, posId, posName, status, paymentType, fundingSource, guestCheckout, createdAt, validUntil, completedAt, reservedUntil, transactions);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public String getPosId() {
        return posId;
    }

    public String getPosName() {
        return posName;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public boolean isGuestCheckout() {
        return guestCheckout;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}

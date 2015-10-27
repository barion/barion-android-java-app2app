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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Transaction implements Serializable {

    private String transactionId;
    private Date time;
    private int amount;
    private String fromName;
    private String fromEmailAddress;
    private String toName;
    private String toEmailAddress;
    private int fromBalance;
    private String comment;
    private boolean commissionBySender;
    private String status;
    private String currency;
    private String transactionType;
    private String relatedTransactionId;
    private String shopName;
    private boolean isParkingTransaction;
    private String shopGuid;
    private ArrayList<Transaction> relatedTransactions;
    private String shopTransactionId;

    private Transaction(String transactionId, Date time, int amount, String fromName, String fromEmailAddress, String toName, String toEmailAddress, int fromBalance, String comment, boolean commissionBySender, String status, String currency, String transactionType, String relatedTransactionId, String shopName, boolean isParkingTransaction, String shopGuid, ArrayList<Transaction> relatedTransactions, String shopTransactionId) {
        this.transactionId = transactionId;
        this.time = time;
        this.amount = amount;
        this.fromName = fromName;
        this.fromEmailAddress = fromEmailAddress;
        this.toName = toName;
        this.toEmailAddress = toEmailAddress;
        this.fromBalance = fromBalance;
        this.comment = comment;
        this.commissionBySender = commissionBySender;
        this.status = status;
        this.currency = currency;
        this.transactionType = transactionType;
        this.relatedTransactionId = relatedTransactionId;
        this.shopName = shopName;
        this.isParkingTransaction = isParkingTransaction;
        this.shopGuid = shopGuid;
        this.relatedTransactions = relatedTransactions;
        this.shopTransactionId = shopTransactionId;
    }

    public static Transaction fromJsonObject(JSONObject object) {
        if (object == null) return null;

        String transactionId = object.optString("TransactionId");
        Date time = Utils.parseJsonDate(object, "Time");
        int amount = object.optInt("Amount");
        String fromName = object.optString("FromName");
        String fromEmailAddress = object.optString("FromEmailAddress");
        String toName = object.optString("ToName");
        String toEmailAddress = object.optString("ToEmailAddress");
        int fromBalance = object.optInt("FromBalance");
        String comment = object.optString("Comment");
        boolean commissionBySender = object.optBoolean("CommissionBySender", false);
        String status = object.optString("Status");
        String currency = object.optString("Currency");
        String transactionType = object.optString("TransactionType");
        String relatedTransactionId = object.optString("RelatedTransactionId");
        String shopName = object.optString("ShopName");
        boolean isParkingTransaction = object.optBoolean("IsParkingTransaction", false);
        String shopGuid = object.optString("ShopGuid");
        ArrayList<Transaction> relatedTransactions = new ArrayList<>();
        JSONArray relatedArray = object.optJSONArray("RelatedTransactions");
        if (relatedArray != null) {
            for (int i = 0; i < relatedArray.length(); ++i) {
                try {
                    JSONObject relatedObject = relatedArray.getJSONObject(i);
                    Transaction related = Transaction.fromJsonObject(relatedObject);
                    if (related != null) relatedTransactions.add(related);
                } catch (JSONException x) {
                }
            }
        }
        String shopTransactionId = object.optString("ShopTransactionId");
        return new Transaction(transactionId, time, amount, fromName, fromEmailAddress, toName, toEmailAddress, fromBalance, comment, commissionBySender, status, currency, transactionType, relatedTransactionId, shopName, isParkingTransaction, shopGuid, relatedTransactions, shopTransactionId);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }

    public String getFromName() {
        return fromName;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public String getToName() {
        return toName;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public int getFromBalance() {
        return fromBalance;
    }

    public String getComment() {
        return comment;
    }

    public boolean isCommissionBySender() {
        return commissionBySender;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public String getShopName() {
        return shopName;
    }

    public boolean isParkingTransaction() {
        return isParkingTransaction;
    }

    public String getShopGuid() {
        return shopGuid;
    }

    public ArrayList<Transaction> getRelatedTransactions() {
        return relatedTransactions;
    }

    public String getShopTransactionId() {
        return shopTransactionId;
    }
}

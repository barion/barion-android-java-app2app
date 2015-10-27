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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class PaymentTransaction implements Serializable {

    private String posTransactionId;
    private String transactionId;
    private String status;
    private Date transactionTime;
    private String relatedId;

    private PaymentTransaction(String posTransactionId, String transactionId, String status, Date transactionTime, String relatedId) {
        this.posTransactionId = posTransactionId;
        this.transactionId = transactionId;
        this.status = status;
        this.transactionTime = transactionTime;
        this.relatedId = relatedId;
    }

    public static PaymentTransaction fromJsonObject(JSONObject object) {
        if (object == null) return null;

        String fTId = object.optString("POSTransactionId");
        String sTId = object.optString("TransactionId");
        String status = object.optString("Status");
        Date sTDate = Utils.parseJsonDate(object, "TransactionTime");
        String relatedId = object.optString("RelatedId");

        return new PaymentTransaction(fTId, sTId, status, sTDate, relatedId);
    }

    public String getPosTransactionId() {
        return posTransactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public String getRelatedId() {
        return relatedId;
    }
}

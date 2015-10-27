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

public class BarionGetPaymentStateResponse implements Serializable {

    private PaymentState paymentState;
    private ArrayList<BarionError> errors;

    private BarionGetPaymentStateResponse(PaymentState paymentState, ArrayList<BarionError> errors) {
        this.paymentState = paymentState;
        this.errors = errors;
    }

    public static BarionGetPaymentStateResponse fromJsonObject(JSONObject object) {
        if (object == null) return null;

        ArrayList<BarionError> errors = new ArrayList<>();
        JSONArray errorsArray = object.optJSONArray("Errors");
        if (errorsArray != null) {
            for (int i = 0; i < errorsArray.length(); ++i) {
                try {
                    JSONObject errorObject = errorsArray.getJSONObject(i);
                    BarionError error = BarionError.fromJsonObject(errorObject);
                    if (error != null) errors.add(error);
                } catch (JSONException x) {
                }
            }
        }
        PaymentState state = null;
        if (errors.size() == 0) {
            state = PaymentState.fromJsonObject(object);
        }

        return new BarionGetPaymentStateResponse(state, errors);
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public ArrayList<BarionError> getErrors() {
        return errors;
    }
}

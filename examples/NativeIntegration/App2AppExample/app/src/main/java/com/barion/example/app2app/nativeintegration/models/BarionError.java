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

import org.json.JSONObject;

import java.io.Serializable;

public class BarionError implements Serializable {

    private String errorCode;
    private String title;
    private String description;

    private BarionError(String errorCode, String title, String description) {
        this.errorCode = errorCode;
        this.title = title;
        this.description = description;
    }

    public static BarionError fromJsonObject(JSONObject object) {
        if (object == null) return null;

        String errorCode = object.optString("ErrorCode");
        String title = object.optString("Title");
        String description = object.optString("Description");

        return new BarionError(errorCode, title, description);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

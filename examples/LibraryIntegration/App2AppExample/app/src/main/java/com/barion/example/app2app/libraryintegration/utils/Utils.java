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
package com.barion.example.app2app.libraryintegration.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Date parseJsonDate(JSONObject object, String tag) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(object.optString(tag));
        } catch (ParseException e) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(object.optString(tag));
            } catch (ParseException ex) {
                date = null;
            }
        }
        return date;
    }

    public static boolean canPayWithBarionApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo barionInfo = packageManager.getPackageInfo("barion.app", PackageManager.GET_META_DATA);
            if (barionInfo.versionName.contains("b") || barionInfo.versionName.contains("d"))
                return true;
            String barionVersionInfo = barionInfo.versionName;
            String[] actualBarionVersionCodeParts = barionVersionInfo.split("\\.");
            String[] requiredBarionVersionCodeParts = new String[]{"2", "1", "12"};
            return isVersionAReachedB(actualBarionVersionCodeParts, requiredBarionVersionCodeParts);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static boolean isVersionAReachedB(String[] versionA, String[] versionB) {
        boolean hasReachedMinimum = false;

        for (int i = 0; i < versionA.length; ++i) {
            try {
                int bPart = Integer.parseInt(versionB[i]);
                int aPart = Integer.parseInt(versionA[i]);

                if (!hasReachedMinimum) {
                    if (aPart > bPart || (i == (versionA.length - 1) && aPart >= bPart))
                        hasReachedMinimum = true;
                    else if (aPart < bPart)
                        return false;
                }
            } catch (NumberFormatException nfex) {
                return false;
            }
        }
        return hasReachedMinimum;
    }
}

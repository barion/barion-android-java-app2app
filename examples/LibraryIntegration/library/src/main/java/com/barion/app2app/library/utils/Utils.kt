/**
 * Copyright 2023 Barion Payment Inc. All Rights Reserved.
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
package com.barion.app2app.library.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal fun JSONObject.parseJsonDate(tag: String?): Date? = try {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(optString(tag))
} catch (e: ParseException) {
    try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(optString(tag))
    } catch (ex: ParseException) {
        null
    }
}

internal fun Date.formatDate(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(this)


internal fun Context.openUrl(url: String) {
    try {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this, Uri.parse(url))
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(this, "Not found any app to load URL", Toast.LENGTH_LONG).show()
    }
}
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
package com.barion.example.app2app.nativeintegration.network

import android.util.Log
import com.barion.example.app2app.nativeintegration.EnvironmentProperties
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class BarionUrlConnectionDelegate(
    private val path: String,
    private val query: String = ""
) {

    fun callPost(json: String?): JSONObject? = getUrlConnection().run {
        requestMethod = REQUEST_METHOD_POST
        doOutput = true
        return call(json)
    }

    fun callGet(): JSONObject? = getUrlConnection().run {
        requestMethod = REQUEST_METHOD_GET
        return call()
    }

    private fun HttpURLConnection.call(json: String? = null): JSONObject? {
        if (requestMethod == REQUEST_METHOD_POST && json != null) {
            outputStream.use { os ->
                val out = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                out.write(json)
                out.flush()
            }
        }

        if (errorStream != null) {
            printDetailedServerErrorMessage()
        }

        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
            Log.d(LOG_TAG, "Pretty Printed JSON:$response")
            JSONObject(response)
        } else {
            Log.e(LOG_TAG, "HTTP Error:$responseCode $responseMessage")
            null
        }
    }

    private fun HttpURLConnection.printDetailedServerErrorMessage() {
        val br = BufferedReader(InputStreamReader(errorStream))
        var strCurrentLine: String?
        while (br.readLine().also { strCurrentLine = it } != null) {
            Log.d(LOG_TAG, "HTTP Error detailed message: $strCurrentLine")
        }
    }

    private fun getUrlConnection(): HttpURLConnection {
        val url = URL(EnvironmentProperties.urlBase + path + query)

        return url.openConnection().apply {
            setRequestProperty("Content-Type", "application/json")
        } as HttpURLConnection
    }
}

private val LOG_TAG = BarionUrlConnectionDelegate::class.simpleName
private const val REQUEST_METHOD_POST = "POST"
private const val REQUEST_METHOD_GET = "GET"
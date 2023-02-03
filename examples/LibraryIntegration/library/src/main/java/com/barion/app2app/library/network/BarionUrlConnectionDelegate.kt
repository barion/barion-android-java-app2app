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
package com.barion.app2app.library.network

import android.util.Log
import com.barion.app2app.library.BarionEnvironment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

internal class BarionUrlConnectionDelegate(
    environment: BarionEnvironment,
    private val path: String,
    private val query: String = ""
) {

    private val environmentProperties = EnvironmentProperties.getEnvironmentProperties(environment)

    fun callPost(json: String?): JSONObject = getUrlConnection().run {
        requestMethod = REQUEST_METHOD_POST
        doOutput = true
        return call(json)
    }

    fun callGet(): JSONObject = getUrlConnection().run {
        requestMethod = REQUEST_METHOD_GET
        return call()
    }

    private fun HttpURLConnection.call(json: String? = null): JSONObject {
        Log.d(LOG_TAG, "HTTP REQUEST ($requestMethod) :$url")

        if (requestMethod == REQUEST_METHOD_POST && json != null) {
            outputStream.use { os ->
                val out = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                out.write(json)
                out.flush()
            }
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw HttpException(
                code = responseCode,
                message = responseMessage,
                response = getDetailedServerErrorMessage()
            )
        }

        val response = inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
        Log.d(LOG_TAG, "Pretty Printed JSON:$response")

        return JSONObject(response)
    }

    private fun HttpURLConnection.getDetailedServerErrorMessage(): String? {
        errorStream ?: return null

        val br = BufferedReader(InputStreamReader(errorStream))
        return br.readLine()
    }

    private fun getUrlConnection(): HttpURLConnection {
        val url = URL(environmentProperties.baseUrl + path + query)

        return url.openConnection().apply {
            setRequestProperty("Content-Type", "application/json")
            connectTimeout = NetworkConstants.CONNECT_TIMEOUT_SEC * 1000
            readTimeout = NetworkConstants.READ_TIMEOUT_SEC * 1000
        } as HttpURLConnection
    }
}

private val LOG_TAG = BarionUrlConnectionDelegate::class.simpleName
private const val REQUEST_METHOD_POST = "POST"
private const val REQUEST_METHOD_GET = "GET"
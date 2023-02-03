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
package com.barion.app2app.library

import android.content.Context
import com.barion.app2app.library.model.BarionGetPaymentStateRequest
import com.barion.app2app.library.model.BarionGetPaymentStateResponse
import com.barion.app2app.library.model.BarionStartPaymentRequest
import com.barion.app2app.library.model.BarionStartPaymentResponse
import com.barion.app2app.library.network.BarionApi
import com.barion.app2app.library.utils.openUrl

class Barion {

    var environment: BarionEnvironment = BarionEnvironment.LIVE

    fun startPayment(context: Context, request: BarionStartPaymentRequest): BarionStartPaymentResponse {
        val response = BarionApi.startPayment(environment, request)

        if (response.errors.isEmpty()) {
            context.openUrl(response.gatewayUrl)
        }

        return response
    }

    fun getPaymentState(request: BarionGetPaymentStateRequest): BarionGetPaymentStateResponse {
        return BarionApi.getPaymentState(environment, request)
    }

    companion object {
        private var instance: Barion? = null

        @JvmName("getInstance")
        fun getInstance(): Barion {
            if (instance == null) instance =
                Barion()
            return instance!!
        }
    }
}
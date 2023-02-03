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

import com.barion.app2app.library.BarionEnvironment
import com.barion.app2app.library.model.BarionGetPaymentStateRequest
import com.barion.app2app.library.model.BarionGetPaymentStateResponse
import com.barion.app2app.library.model.BarionStartPaymentRequest
import com.barion.app2app.library.model.BarionStartPaymentResponse

internal object BarionApi {

    fun startPayment(environment: BarionEnvironment, request: BarionStartPaymentRequest): BarionStartPaymentResponse {
        val con = BarionUrlConnectionDelegate(
            environment = environment,
            path = NetworkConstants.PATH_START_PAYMENT
        )

        return con.callPost(request.toJSONObject().toString())
            .toBarionStartPaymentResponse()
    }

    fun getPaymentState(environment: BarionEnvironment, request: BarionGetPaymentStateRequest): BarionGetPaymentStateResponse {
        val con = BarionUrlConnectionDelegate(
            environment = environment,
            path = NetworkConstants.PATH_GET_NETWORK_STATE,
            query = "?POSKey=${request.posKey}&PaymentId=${request.paymentId}"
        )

        return con.callGet()
            .toBarionGetPaymentStateResponse()
    }
}
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
package com.barion.app2app.library.example

import com.barion.app2app.library.model.Product

@Suppress("MemberVisibilityCanBePrivate")
object EnvironmentProperties {
    val PosKey: String
        get() = "c2099d08-c7f0-42b1-8605-c755b7d83be6"

    val Payee: String
        get() = "dev.gerlei@gmail.com"

    val Products: List<Product>
        get() = listOf(
            Product(
                name = "The history of money",
                description = "The evolution of money from the stoneage to Barion",
                quantity = 1.0,
                unit = "db",
                price = 40.0,
                sKU = "APP2APPDEMO_PENZTORT"
            ),
            Product(
                name = "How to make a fortune with Barion",
                description = "Tips to get rich",
                quantity = 1.0,
                unit = "db",
                price = 20.0,
                sKU = "APP2APPDEMO_SOKPENZ"
            ),
            Product(
                name = "The begginers guide to accepting bank cards",
                description = "How to become bank card acceptors in five minutes with Barion",
                quantity = 1.0,
                unit = "db",
                price = 140.0,
                sKU = "APP2APPDEMO_BANKKARI"
            )
        )


    val ExampleRequest = com.barion.app2app.library.model.BarionStartPaymentRequest(
        posKey = PosKey,
        paymentType = com.barion.app2app.library.model.PaymentType.IMMEDIATE,
        guestCheckOut = true,
        fundingSources = listOf("All"),
        paymentRequestId = "12345",
        locale = "hu-HU",
        currency = "EUR",
        transactions = listOf(
            com.barion.app2app.library.model.PaymentTransaction(
                posTransactionId = "55",
                payee = Payee,
                total = 200,
                comment = "comment",
                products = Products
            )
        ),
        redirectUrl = NetworkConstants.REDIRECT_URL,
    )
}
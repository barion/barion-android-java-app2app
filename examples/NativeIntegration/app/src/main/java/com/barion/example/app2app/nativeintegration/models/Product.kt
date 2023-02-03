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
package com.barion.example.app2app.nativeintegration.models

data class Product(
    var name: String,
    var description: String,
    var quantity: Float,
    var unit: String,
    var price: Float,
    var sKU: String,
)

object ProductProvider {

    fun getList() = listOf(
        Product(
            name = "The history of money",
            description = "The evolution of money from the stoneage to Barion",
            quantity = 1f,
            unit = "db",
            price = 40f,
            sKU = "APP2APPDEMO_PENZTORT"
        ),
        Product(
            name = "How to make a fortune with Barion",
            description = "Tips to get rich",
            quantity = 1f,
            unit = "db",
            price = 20f,
            sKU = "APP2APPDEMO_SOKPENZ"
        ),
        Product(
            name = "The begginers guide to accepting bank cards",
            description = "How to become bank card acceptors in five minutes with Barion",
            quantity = 1f,
            unit = "db",
            price = 140f,
            sKU = "APP2APPDEMO_BANKKARI"
        )
    )
}
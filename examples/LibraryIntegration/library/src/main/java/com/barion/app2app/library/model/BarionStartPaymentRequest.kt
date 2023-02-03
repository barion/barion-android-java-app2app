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
package com.barion.app2app.library.model

import java.util.*

data class BarionStartPaymentRequest(
    val posKey: String,
    val paymentType: PaymentType,
    val guestCheckOut: Boolean,
    val fundingSources: List<String>,
    val paymentRequestId: String,
    val transactions: List<PaymentTransaction>,
    val redirectUrl: String,
    val locale: String,
    val currency: String,
    val reservationPeriod: Date? = null,
    val delayedCapturePeriod: Date? = null,
    val paymentWindow: Date? = null,
    val initiateRecurrence: Boolean? = null,
    val recurrenceId: String? = null,
    val payerHint: String? = null,
    val cardHolderNameHint: String? = null,
    val recurrenceType: String? = null,
    val traceId: String? = null,
    val orderNumber: String? = null,
    val shippingAddress: ShippingAddress? = null,
    val payerPhoneNumber: String? = null,
    val payerWorkPhoneNumber: String? = null,
    val payerHomeNumber: String? = null,
    val billingAddress: BillingAddress? = null,
    val payerAccountInformation: PayerAccountInformation? = null,
    val purchaseInformation: PurchaseInformation? = null,
    val challengePreference: ChallengePreference? = null,
)
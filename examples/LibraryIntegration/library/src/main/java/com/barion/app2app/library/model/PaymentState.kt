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

data class PaymentState(
    val paymentId: String,
    val paymentRequestId: String,
    val posId: String,
    val posName: String,
    val posOwnerEmail: String,
    val posOwnerCountry: String,
    val status: PaymentStatus,
    val paymentType: PaymentType,
    val allowedFundingSources: List<String>,
    val fundingSource: String,
    val fundingInformation: FundingInformation,
    val reccuranceType: ReccuranceType,
    val traceId: String,
    val isGuestCheckout: Boolean,
    val createdAt: Date?,
    val startedAt: Date?,
    val completedAt: Date?,
    val validUntil: Date?,
    val reservedUntil: Date?,
    val delayedCaptureUntil: Date?,
    val transactions: List<Transaction>,
    val total: Double,
    val currency: String,
    val suggestedLocale: String,
    val fraudRiskScore: String,
    val callbackUrl: String,
    val redirectUrl: String,
)
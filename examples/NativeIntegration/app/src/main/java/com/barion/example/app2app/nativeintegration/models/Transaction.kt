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

import java.util.*

data class Transaction(
    val transactionId: String,
    val time: Date?,
    val amount: Int,
    val fromName: String,
    val fromEmailAddress: String,
    val toName: String,
    val toEmailAddress: String,
    val fromBalance: Int,
    val comment: String,
    val isCommissionBySender: Boolean,
    val status: String,
    val currency: String,
    val transactionType: String,
    val relatedTransactionId: String,
    val shopName: String,
    val isParkingTransaction: Boolean,
    val shopGuid: String,
    val relatedTransactions: List<Transaction>,
    val shopTransactionId: String,
)
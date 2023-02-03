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
import com.barion.example.app2app.nativeintegration.models.*
import com.barion.example.app2app.nativeintegration.utils.parseJsonDate
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun JSONObject.toBarionGetPaymentStateResponse(): BarionGetPaymentStateResponse {
    val errors = toBarionErrorList()

    val state = if (errors.isEmpty()) {
        toPaymentState()
    } else {
        null
    }

    return BarionGetPaymentStateResponse(
        paymentState = state,
        errors = errors
    )
}

fun JSONObject.toBarionErrorList() = buildList {
    val errorsArray = optJSONArray("Errors")
    if (errorsArray != null) {
        for (i in 0 until errorsArray.length()) {
            try {
                val error = errorsArray
                    .getJSONObject(i)
                    ?.toBarionError()
                if (error != null) add(error)
            } catch (e: JSONException) {
                Log.e(BarionGetPaymentStateResponse::class.simpleName, e.message, e)
            }
        }
    }
}

fun JSONObject.toBarionError(): BarionError {
    return BarionError(
        errorCode = optString("ErrorCode"),
        title = optString("Title"),
        description = optString("Description"),
    )
}

fun BarionStartPaymentRequest.toJSONObject(): JSONObject {
    val fundingSourcesJSONArray = JSONArray().apply {
        fundingSources.onEach { put(it) }
    }

    val transactionsJSONArray = JSONArray().apply {
        transactions.onEach { transaction -> put(transaction.toJSONObject()) }
    }

    return JSONObject().apply {
        put("POSKey", posKey)
        put("PaymentType", paymentType)
        put("GuestCheckOut", guestCheckOut)
        put("FundingSources", fundingSourcesJSONArray)
        put("PaymentRequestId", paymentRequestId)
        put("Locale", locale)
        put("Currency", currency)
        put("Transactions", transactionsJSONArray)
        put("RedirectUrl", redirectUrl)
    }
}

fun JSONObject.toBarionStartPaymentResponse(): BarionStartPaymentResponse {
    val paymentTransactions = buildList {
        optJSONArray("Transactions")?.let { array ->
            for (i in 0 until array.length()) {
                try {
                    array.getJSONObject(i)
                        ?.toPaymentTransaction()
                        ?.let { add(it) }
                } catch (e: JSONException) {
                    Log.e(BarionStartPaymentResponse::class.simpleName, e.message, e)
                }
            }
        }
    }

    return BarionStartPaymentResponse(
        paymentId = optString("PaymentId"),
        paymentRequestId = optString("PaymentRequestId"),
        status = optString("Status"),
        qrUrl = optString("QRUrl"),
        transactions = paymentTransactions,
        gatewayUrl = optString("GatewayUrl"),
        callbackUrl = optString("CallbackUrl"),
        threeDSAuthClientData = optString("ThreeDSAuthClientData"),
        traceId = optString("TraceId"),
        errors = toBarionErrorList()
    )
}

fun JSONObject.toPaymentState() = PaymentState(
    paymentId = optString("PaymentId"),
    paymentRequestId = optString("PaymentRequestId"),
    posId = optString("POSId"),
    posName = optString("POSName"),
    status = optString("Status"),
    paymentType = optString("PaymentType"),
    fundingSource = optString("FundingSource"),
    isGuestCheckout = optBoolean("GuestCheckout"),
    createdAt = parseJsonDate("CreatedAt"),
    validUntil = parseJsonDate("ValidUntil"),
    completedAt = parseJsonDate("CompletedAt"),
    reservedUntil = parseJsonDate("ReservedUntil"),
    transactions = toPaymentTransactionList()
)

fun JSONObject.toPaymentTransaction() = PaymentTransaction(
    posTransactionId = "",
    payee = "",
    total = 0,
    comment = null,
    products = listOf()
)

fun PaymentTransaction.toJSONObject(): JSONObject {
    val productsJSONArray = JSONArray().apply {
        products.onEach { put(it.toJSONObject()) }
    }

    return JSONObject().apply {
        put("POSTransactionId", posTransactionId)
        put("Payee", payee)
        put("Total", total)
        put("Comment", comment)
        put("Items", productsJSONArray)
    }
}

fun Product.toJSONObject() = JSONObject().apply {
    put("Name", name)
    put("Description", description)
    put("Quantity", quantity)
    put("Unit", unit)
    put("Price", price)
    put("SKU", sKU)
}

fun JSONObject.toTransaction(): Transaction {
    return Transaction(
        transactionId = optString("TransactionId"),
        time = parseJsonDate("Time"),
        amount = optInt("Amount"),
        fromName = optString("FromName"),
        fromEmailAddress = optString("FromEmailAddress"),
        toName = optString("ToName"),
        toEmailAddress = optString("ToEmailAddress"),
        fromBalance = optInt("FromBalance"),
        comment = optString("Comment"),
        isCommissionBySender = optBoolean("CommissionBySender", false),
        status = optString("Status"),
        currency = optString("Currency"),
        transactionType = optString("TransactionType"),
        relatedTransactionId = optString("RelatedTransactionId"),
        shopName = optString("ShopName"),
        isParkingTransaction = optBoolean("IsParkingTransaction", false),
        shopGuid = optString("ShopGuid"),
        shopTransactionId = optString("ShopTransactionId"),
        relatedTransactions = toPaymentTransactionList()
    )
}

fun JSONObject.toPaymentTransactionList() = buildList {
    optJSONArray("RelatedTransactions")?.let { array ->
        for (i in 0 until array.length()) {
            try {
                array.getJSONObject(i)
                    ?.toTransaction()
                    ?.let { add(it) }
            } catch (e: JSONException) {
                Log.e(Transaction::class.simpleName, e.message, e)
            }
        }
    }
}
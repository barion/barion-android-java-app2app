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
import com.barion.app2app.library.model.*
import com.barion.app2app.library.utils.formatDate
import com.barion.app2app.library.utils.parseJsonDate
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal fun JSONObject.toBarionGetPaymentStateResponse(): BarionGetPaymentStateResponse {
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

internal fun JSONObject.toBarionErrorList() = buildList {
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

internal fun JSONObject.toBarionError(): BarionError {
    return BarionError(
        errorCode = optString("ErrorCode"),
        title = optString("Title"),
        description = optString("Description"),
    )
}

internal fun JSONObject.toBarionStartPaymentResponse(): BarionStartPaymentResponse {
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

internal fun JSONObject.toPaymentState() = PaymentState(
    paymentId = optString("PaymentId"),
    paymentRequestId = optString("PaymentRequestId"),
    posId = optString("POSId"),
    posName = optString("POSName"),
    posOwnerEmail = optString("POSOwnerEmail"),
    posOwnerCountry = optString("POSOwnerCountry"),
    status = PaymentStatus.parse(optString("Status")),
    paymentType = PaymentType.parse(optString("PaymentType")),
    allowedFundingSources = buildList {
        optJSONArray("AllowedFundingSources")!!.let { jsonArray ->
            for (i in 0 until jsonArray.length()) {
                add(jsonArray.getString(i))
            }
        }
    },
    fundingSource = optString("FundingSource"),
    fundingInformation = toFundingInformation(),
    reccuranceType = ReccuranceType.parse(optInt("ReccuranceType")),
    traceId = optString("TraceId"),
    isGuestCheckout = optBoolean("GuestCheckout"),
    createdAt = parseJsonDate("CreatedAt"),
    startedAt = parseJsonDate("StartedAt"),
    completedAt = parseJsonDate("CompletedAt"),
    validUntil = parseJsonDate("ValidUntil"),
    reservedUntil = parseJsonDate("ReservedUntil"),
    delayedCaptureUntil = parseJsonDate("DelayedCaptureUntil"),
    transactions = toPaymentTransactionList(),
    total = optDouble("Total"),
    currency = optString("Currency"),
    suggestedLocale = optString("SuggestedLocale"),
    fraudRiskScore = optString("FraudRiskScore"),
    callbackUrl = optString("CallbackUrl"),
    redirectUrl = optString("RedirectUrl"),
)

internal fun JSONObject.toFundingInformation() = FundingInformation(
    bankCard = toBankCard(),
    authorizationCode = optString("AuthorizationCode"),
    processResult = optString("ProcessResult"),
)

internal fun JSONObject.toBankCard() = BankCard(
    maskedPan = optString("MaskedPan"),
    bankCardType = CardType.parse(optInt("BankCardType")),
    validThruYear = optString("ValidThruYear"),
    validThruMonth = optString("ValidThruMonth"),
)

internal fun JSONObject.toPaymentTransaction(): PaymentTransaction {
    val products = buildList {
        optJSONArray("Products")?.let { array ->
            for (i in 0 until array.length()) {
                try {
                    array.getJSONObject(i)
                        ?.toProduct()
                        ?.let { add(it) }
                } catch (e: JSONException) {
                    Log.e(BarionStartPaymentResponse::class.simpleName, e.message, e)
                }
            }
        }
    }

    return PaymentTransaction(
        posTransactionId = optString("POSTransactionId"),
        payee = optString(""),
        total = optInt("Total"),
        comment = optString("Comment"),
        products = products
    )
}

internal fun JSONObject.toProduct() = Product(
    name = optString("Name"),
    description = optString("Description"),
    quantity = optDouble("Quantity"),
    unit = optString("Unit"),
    price = optDouble("Price"),
    sKU = optString("SKU"),
)

internal fun JSONObject.toTransaction(): Transaction {
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

internal fun JSONObject.toPaymentTransactionList() = buildList {
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

internal fun BarionStartPaymentRequest.toJSONObject(): JSONObject {
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
        reservationPeriod?.let { put("ReservationPeriod", it.formatDate()) }
        delayedCapturePeriod?.let { put("DelayedCapturePeriod", it.formatDate()) }
        paymentWindow?.let { put("PaymentWindow", it.formatDate()) }
        initiateRecurrence?.let { put("InitiateRecurrence", it) }
        recurrenceId?.let { put("RecurrenceId", it) }
        payerHint?.let { put("PayerHint", it) }
        cardHolderNameHint?.let { put("CardHolderNameHint", it) }
        recurrenceType?.let { put("RecurrenceType", it) }
        traceId?.let { put("TraceId", it) }
        orderNumber?.let { put("OrderNumber", it) }
        payerPhoneNumber?.let { put("PayerPhoneNumber", it) }
        payerWorkPhoneNumber?.let { put("PayerWorkPhoneNumber", it) }
        payerHomeNumber?.let { put("PayerHomeNumber", it) }
        shippingAddress?.let { put("ShippingAddress", it.toJSONObject()) }
        billingAddress?.let { put("BillingAddress", it.toJSONObject()) }
        payerAccountInformation?.let { put("PayerAccountInformation", it.toJSONObject()) }
        purchaseInformation?.let { put("PurchaseInformation", it.toJSONObject()) }
    }
}

internal fun PaymentTransaction.toJSONObject(): JSONObject {
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

internal fun ShippingAddress.toJSONObject(): JSONObject {
    return JSONObject().apply {
        put("Country", country)
        put("City", city)
        region?.let { put("Region", region) }
        put("Zip", zip)
        put("Street", street)
        put("Street2", street2)
        put("Street3", street3)
        put("FullName", fullName)
    }
}

internal fun BillingAddress.toJSONObject(): JSONObject {
    return JSONObject().apply {
        put("Country", country)
        put("City", city)
        region?.let { put("Region", region) }
        put("Zip", zip)
        put("Street", street)
        put("Street2", street2)
        put("Street3", street3)
    }
}

internal fun PayerAccountInformation.toJSONObject(): JSONObject {
    return JSONObject().apply {
        accountId?.let { put("AccountId", it) }
        accountCreated?.let { put("AccountCreated", it.formatDate()) }
        accountCreationIndicator?.let { put("AccountCreationIndicator", it.value) }
        accountLastChanged?.let { put("AccountLastChanged", it.formatDate()) }
        accountLastChangedIndicator?.let { put("AccountLastChangedIndicator", it.value) }
        passwordLastChanged?.let { put("PasswordLastChanged", it.formatDate()) }
        passwordLastChangedIndicator?.let { put("PasswordLastChangedIndicator", it.value) }
        purchasesInTheLast6Months?.let { put("PurchasesInTheLast6Months", it) }
        shippingAddressAdded?.let { put("ShippingAddressAdded", it.formatDate()) }
        shippingAddressUsageIndicator?.let { put("ShippingAddressUsageIndicator", it.value) }
        provisionAttempts?.let { put("ProvisionAttempts", it) }
        transactionalActivityPerDay?.let { put("AccountCreated", it) }
        transactionalActivityPerYear?.let { put("TransactionalActivityPerYear", it) }
        paymentMethodAdded?.let { put("PaymentMethodAdded", it.formatDate()) }
        suspiciousActivityIndicator?.let { put("SuspiciousActivityIndicator", it.value) }
    }
}

internal fun PurchaseInformation.toJSONObject(): JSONObject {
    return JSONObject().apply {
        deliveryTimeframe?.let { put("DeliveryTimeframe", it.value) }
        deliveryEmailAddress?.let { put("DeliveryEmailAddress", it) }
        preOrderDate?.let { put("PreOrderDate", it.formatDate()) }
        availabilityIndicator?.let { put("AvailabilityIndicator", it.value) }
        reOrderIndicator?.let { put("ReOrderIndicator", it.value) }
        shippingAddressIndicator?.let { put("ShippingAddressIndicator", it.value) }
        recurringExpiry?.let { put("RecurringExpiry", it.formatDate()) }
        recurringFrequency?.let { put("RecurringFrequency", it) }
        purchaseType?.let { put("PurchaseType", it.value) }
        giftCardPurchase?.let { put("GiftCardPurchase", it.toJSONObject()) }
        purchaseDate?.let { put("GiftCardPurchase", it.formatDate()) }
    }
}

internal fun Product.toJSONObject() = JSONObject().apply {
    put("Name", name)
    put("Description", description)
    put("Quantity", quantity)
    put("Unit", unit)
    put("Price", price)
    put("SKU", sKU)
}

internal fun GiftCardPurchase.toJSONObject() = JSONObject().apply {
    put("Account", account)
    put("Count", count)
}
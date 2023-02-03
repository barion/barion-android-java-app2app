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
package com.barion.app2app.library.example.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.barion.app2app.library.Barion
import com.barion.app2app.library.example.EnvironmentProperties
import com.barion.app2app.library.example.R
import com.barion.app2app.library.example.fragments.ShopFragment
import com.barion.app2app.library.example.utils.networkErrorDialog
import com.barion.app2app.library.model.BarionError
import com.barion.app2app.library.model.BarionGetPaymentStateRequest
import com.barion.app2app.library.model.PaymentState
import com.barion.app2app.library.model.PaymentStatus
import com.barion.app2app.library.network.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PaymentResultActivity : AppCompatActivity() {

    private val titleTextView: TextView
        get() = findViewById(R.id.resulttitle)

    private val detailTextView: TextView
        get() = findViewById(R.id.resultdetail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.run {
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.activity_title)
            setLogo(ContextCompat.getDrawable(this@PaymentResultActivity, R.drawable.icontext))
        }

        apiCall()
    }

    private fun apiCall() {
        lifecycleScope.launch(Dispatchers.IO) {
            setVisibleLoadingProgress(true)

            try {
                val request = BarionGetPaymentStateRequest(posKey = EnvironmentProperties.PosKey, paymentId = getPaymentId())

                val response = Barion
                    .getInstance()
                    .getPaymentState(request)

                if (response.errors.isEmpty()) {
                    processPaymentState(response.paymentState)
                } else {
                    processBarionErrors(response.errors)
                }
            } catch (exception: HttpException) {
                Log.d(ShopFragment::class.simpleName, exception.message.toString())
                networkErrorDialog()
            } catch (exception: IOException) {
                Log.d(ShopFragment::class.simpleName, exception.message.toString())
                networkErrorDialog()
            }

            setVisibleLoadingProgress(false)
        }
    }

    private fun getPaymentId() = intent.data!!.query!!.replace("paymentId=", "")

    private suspend fun processBarionErrors(errors: List<BarionError>) = withContext(Dispatchers.Main) {
        setVisibleLoadingProgress(false)
        titleTextView.setText(R.string.result_fail_title)
        detailTextView.text = errors[0].description
    }

    private suspend fun processPaymentState(state: PaymentState?) = withContext(Dispatchers.Main) {
        setVisibleLoadingProgress(false)
        val status = state!!.status
        titleTextView.text = "${getString(R.string.stateofpayment)} $status"

        val detailId: Int = when (status) {
            PaymentStatus.PREPARED -> R.string.detail_prepared
            PaymentStatus.STARTED -> R.string.detail_started
            PaymentStatus.IN_PROGRESS -> R.string.detail_inprogress
            PaymentStatus.WAITING -> R.string.detail_waiting
            PaymentStatus.RESERVED -> R.string.detail_reserved
            PaymentStatus.AUTHORIZED -> R.string.detail_authorized
            PaymentStatus.CANCELED -> R.string.detail_canceled
            PaymentStatus.SUCCEEDED -> R.string.detail_succeeded
            PaymentStatus.FAILED -> R.string.detail_failed
            PaymentStatus.PARTIALLY_SUCCEEDED -> R.string.detail_partiallysucceeded
            PaymentStatus.EXPIRED -> R.string.detail_expired

            else -> R.string.unknown
        }
        detailTextView.text = getString(detailId)
    }

    private suspend fun setVisibleLoadingProgress(isVisible: Boolean) = withContext(Dispatchers.Main) {
        findViewById<View>(R.id.disablingLoadingView)?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
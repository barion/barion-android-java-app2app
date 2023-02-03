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
package com.barion.example.app2app.nativeintegration.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.barion.example.app2app.nativeintegration.EnvironmentProperties
import com.barion.example.app2app.nativeintegration.R
import com.barion.example.app2app.nativeintegration.models.BarionError
import com.barion.example.app2app.nativeintegration.models.BarionGetPaymentStateRequest
import com.barion.example.app2app.nativeintegration.models.PaymentState
import com.barion.example.app2app.nativeintegration.network.BarionApi
import com.barion.example.app2app.nativeintegration.utils.networkErrorDialog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PaymentResultActivity : AppCompatActivity() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

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
        lifecycleScope.launch(ioDispatcher) {
            setVisibleLoadingProgress(true)

            val response = BarionApi.getPaymentState(
                BarionGetPaymentStateRequest(
                    posKey = EnvironmentProperties.PosKey,
                    paymentId = getPaymentId()
                )
            )

            if (response == null) {
                networkErrorDialog(mainDispatcher)
                return@launch
            }

            if (response.errors.isEmpty()) {
                processPaymentState(response.paymentState)
            } else {
                processBarionErrors(response.errors)
            }

            setVisibleLoadingProgress(false)
        }
    }

    private fun getPaymentId() = intent.data!!.query!!.replace("paymentId=", "")

    private suspend fun processBarionErrors(errors: List<BarionError>) = withContext(mainDispatcher) {
        setVisibleLoadingProgress(false)
        titleTextView.setText(R.string.result_fail_title)
        detailTextView.text = errors[0].description
    }

    private suspend fun processPaymentState(state: PaymentState?) = withContext(mainDispatcher) {
        setVisibleLoadingProgress(false)
        val status = state!!.status
        titleTextView.text = "${getString(R.string.stateofpayment)} $status"

        val detailId: Int = when (status.lowercase(Locale.getDefault())) {
            "prepared" -> R.string.detail_prepared
            "started" -> R.string.detail_started
            "inprogress" -> R.string.detail_inprogress
            "waiting" -> R.string.detail_waiting
            "reserved" -> R.string.detail_reserved
            "authorized" -> R.string.detail_authorized
            "canceled" -> R.string.detail_canceled
            "succeeded" -> R.string.detail_succeeded
            "failed" -> R.string.detail_failed
            "partiallysucceeded" -> R.string.detail_partiallysucceeded
            "expired" -> R.string.detail_expired

            else -> R.string.unknown
        }
        detailTextView.text = getString(detailId)
    }

    private suspend fun setVisibleLoadingProgress(isVisible: Boolean) = withContext(mainDispatcher) {
        findViewById<View>(R.id.disablingLoadingView)?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
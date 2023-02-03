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
package com.barion.example.app2app.nativeintegration.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.lifecycle.lifecycleScope
import com.barion.example.app2app.nativeintegration.EnvironmentProperties
import com.barion.example.app2app.nativeintegration.R
import com.barion.example.app2app.nativeintegration.gui.ProductAdapter
import com.barion.example.app2app.nativeintegration.models.BarionError
import com.barion.example.app2app.nativeintegration.models.ProductProvider
import com.barion.example.app2app.nativeintegration.network.BarionApi
import com.barion.example.app2app.nativeintegration.utils.networkErrorDialog
import com.barion.example.app2app.nativeintegration.utils.openUrl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopFragment : ListFragment(), View.OnClickListener {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_shop, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ProductAdapter(requireActivity(), ProductProvider.getList())
        view.findViewById<View>(R.id.payButton).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.payButton) {
            apiCall()
        }
    }

    private fun apiCall() {
        lifecycleScope.launch(ioDispatcher) {
            setVisibleLoadingProgress(true)

            val response = BarionApi.startPayment(EnvironmentProperties.ExampleRequest)

            if (response == null) {
                context?.networkErrorDialog(mainDispatcher)
                return@launch
            }

            if (response.errors.isEmpty()) {
                activity?.openUrl(response.gatewayUrl)
            } else {
                processBarionErrors(response.errors)
            }

            setVisibleLoadingProgress(false)

        }
    }

    private suspend fun processBarionErrors(errors: List<BarionError>) = activity?.run {
        val message = buildString {
            errors.indices.forEach { i ->
                append(errors[i].description).append("\n")
            }
        }

        withContext(mainDispatcher) {
            AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    private suspend fun setVisibleLoadingProgress(isVisible: Boolean) = withContext(mainDispatcher) {
        activity?.findViewById<View>(R.id.disablingLoadingView)?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
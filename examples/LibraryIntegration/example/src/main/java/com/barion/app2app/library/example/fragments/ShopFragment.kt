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
package com.barion.app2app.library.example.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.lifecycle.lifecycleScope
import com.barion.app2app.library.Barion
import com.barion.app2app.library.example.EnvironmentProperties
import com.barion.app2app.library.example.R
import com.barion.app2app.library.example.gui.ProductAdapter
import com.barion.app2app.library.example.utils.networkErrorDialog
import com.barion.app2app.library.model.BarionError
import com.barion.app2app.library.model.ProductProvider
import com.barion.app2app.library.network.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class ShopFragment : ListFragment(), View.OnClickListener {

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
        lifecycleScope.launch(Dispatchers.IO) {
            setVisibleLoadingProgress(true)

            try {
                val response = Barion
                    .getInstance()
                    .startPayment(requireContext(), EnvironmentProperties.ExampleRequest)

                if (response.errors.isNotEmpty()) {
                    processBarionErrors(response.errors)
                }
            } catch (exception: HttpException) {
                Log.d(ShopFragment::class.simpleName, exception.message.toString())
                context?.networkErrorDialog()
            } catch (exception: IOException) {
                Log.d(ShopFragment::class.simpleName, exception.message.toString())
                context?.networkErrorDialog()
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

        withContext(Dispatchers.Main) {
            AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    private suspend fun setVisibleLoadingProgress(isVisible: Boolean) = withContext(Dispatchers.Main) {
        activity?.findViewById<View>(R.id.disablingLoadingView)?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
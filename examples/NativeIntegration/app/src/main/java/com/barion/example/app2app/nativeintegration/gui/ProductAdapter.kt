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
package com.barion.example.app2app.nativeintegration.gui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.barion.example.app2app.nativeintegration.R
import com.barion.example.app2app.nativeintegration.models.Product

class ProductAdapter(context: Context, private val products: List<Product>) : ArrayAdapter<Product?>(context, 0, products) {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cView = convertView

        val holder = if (cView == null || cView.tag == null) {
            cView = inflater.inflate(R.layout.list_product, parent, false)

            val holder = ProductHolder(
                nameTextView = cView.findViewById(R.id.nameTextView),
                unitTextView = cView.findViewById(R.id.unitTextView),
                descriptionTextView = cView.findViewById(R.id.descriptionTextView),
                priceTextView = cView.findViewById(R.id.priceTextView),
            )

            cView.tag = holder

            holder
        } else {
            cView.tag as ProductHolder
        }

        val product = products[position]

        holder.run {
            nameTextView.text = product.name
            unitTextView.text = product.quantity.toInt().toString() + " " + product.unit
            descriptionTextView.text = product.description
            priceTextView.text = product.price.toInt().toString() + " Ft"
        }

        return cView!!
    }

}

private data class ProductHolder(
    var nameTextView: TextView,
    var unitTextView: TextView,
    var descriptionTextView: TextView,
    var priceTextView: TextView
)
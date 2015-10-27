/**
 * Copyright 2015 Barion Payment Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.barion.example.app2app.libraryintegration.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.barion.example.app2app.libraryintegration.R;

import java.util.ArrayList;

import barion.com.barionlibrary.models.BarionProduct;

public class ProductAdapter extends ArrayAdapter<BarionProduct> {

    private ArrayList<BarionProduct> products;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, ArrayList<BarionProduct> products) {
        super(context, 0, products);
        this.products = products;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new ProductHolder();
            convertView = inflater.inflate(R.layout.list_product, parent, false);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.unitTextView = (TextView) convertView.findViewById(R.id.unitTextView);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
            convertView.setTag(holder);
        } else {
            holder = (ProductHolder) convertView.getTag();
        }

        BarionProduct p = products.get(position);

        holder.nameTextView.setText(p.getName());
        holder.unitTextView.setText((int) p.getQuantity() + " " + p.getUnit());
        holder.descriptionTextView.setText(p.getDescription());
        holder.priceTextView.setText((int) p.getPrice() + " Ft");

        return convertView;
    }

    static class ProductHolder {
        TextView nameTextView;
        TextView unitTextView;
        TextView descriptionTextView;
        TextView priceTextView;
    }
}

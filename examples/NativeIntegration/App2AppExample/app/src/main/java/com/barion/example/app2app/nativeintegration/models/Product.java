/**
 * Copyright 2015 Barion Payment Inc. All Rights Reserved.
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
package com.barion.example.app2app.nativeintegration.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    private String name;
    private String description;
    private float quantity;
    private String unit;
    private float price;
    private String SKU;

    private Product(String name, String description, float quantity, String unit, float price, String SKU) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.SKU = SKU;
    }

    public static ArrayList<Product> getProducts() {
        return new ArrayList<Product>() {{
            add(new Product("The history of money", "The evolution of money from the stoneage to Barion", 1f, "db", 40, "APP2APPDEMO_PENZTORT"));
            add(new Product("How to make a fortune with Barion", "Tips to get rich", 1f, "db", 20, "APP2APPDEMO_SOKPENZ"));
            add(new Product("The begginers guide to accepting bank cards", "How to become bank card acceptors in five minutes with Barion", 1f, "db", 140, "APP2APPDEMO_BANKKARI"));
        }};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

}

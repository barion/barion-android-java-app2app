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
package com.barion.app2app.library.model

enum class ShippingAddressIndicator(val value: Int) {
    SHIP_TO_CARDHOLDERS_BILLING_ADDRESS(0),
    SHIP_TO_ANOTHER_VERIFIED_ADDRESS(10),
    SHIP_TO_DIFFERENT_ADDRESS(20),
    SHIP_TO_STORE(30),
    DIGITAL_GOODS(40),
    TRAVEL_AND_EVENT_TICKETS(50),
    OTHER(60),
}
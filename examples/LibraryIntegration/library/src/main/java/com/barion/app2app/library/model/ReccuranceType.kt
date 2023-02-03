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

enum class ReccuranceType(val value: Int) {
    ONE_CLICK_PAYMENT(10),
    MERCHANT_INITIATED_PAYMENT(0),
    RECURRING_PAYMENT(20), ;

    companion object {
        fun parse(str: Int): ReccuranceType = values().first { it.value == str }
    }
}
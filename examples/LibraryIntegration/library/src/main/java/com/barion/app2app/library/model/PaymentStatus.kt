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

enum class PaymentStatus(val value: Int) {
    PREPARED(10),
    STARTED(20),
    IN_PROGRESS(21),
    WAITING(22),
    RESERVED(25),
    AUTHORIZED(26),
    CANCELED(30),
    SUCCEEDED(40),
    FAILED(50),
    PARTIALLY_SUCCEEDED(60),
    EXPIRED(70);

    companion object {
        fun parse(str: String): PaymentStatus = values().first { it.name.equals(str, true)}
    }
}
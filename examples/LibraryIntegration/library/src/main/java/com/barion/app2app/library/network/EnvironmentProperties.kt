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
package com.barion.app2app.library.network

import com.barion.app2app.library.BarionEnvironment

internal enum class EnvironmentProperties(val baseUrl: String) {
    LIVE(baseUrl = "ttps://api.barion.com/"),
    TEST(baseUrl = "https://api.test.barion.com/");

    companion object {
        fun getEnvironmentProperties(environment: BarionEnvironment) = when(environment) {
            BarionEnvironment.LIVE -> LIVE
            BarionEnvironment.TEST -> TEST
        }
    }
}
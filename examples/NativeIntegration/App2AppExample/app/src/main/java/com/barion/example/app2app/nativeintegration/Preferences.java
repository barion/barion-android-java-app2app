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
package com.barion.example.app2app.nativeintegration;

public class Preferences {

    /**
     * Redirect url of the integrator application
     */
    public static final String REDIRECT_URL = "myredirecturl://";

    /* TODO Create your own server side scripts */
    // Server side urls.
    public static final String URL_GETPAYMENTSTATE = "http://your-domain.com/get_barion_payment_state.php";
    public static final String URL_STARTPAYMENT = "http://your-domain.com/generate_barion_payment.php";

}

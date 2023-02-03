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
package com.barion.app2app.library.example.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.barion.app2app.library.Barion
import com.barion.app2app.library.BarionEnvironment
import com.barion.app2app.library.example.R
import com.barion.app2app.library.example.fragments.ShopFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Barion.getInstance().environment = BarionEnvironment.TEST

        supportActionBar?.run {
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.activity_title)
            setLogo(ContextCompat.getDrawable(this@MainActivity, R.drawable.icontext))
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerLayout, ShopFragment())
                .commit()
        }
    }
}
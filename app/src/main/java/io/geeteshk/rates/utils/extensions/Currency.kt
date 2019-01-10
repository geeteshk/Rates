/*
 * Copyright 2019 Geetesh Kalakoti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geeteshk.rates.utils.extensions

import android.content.Context
import androidx.annotation.DrawableRes
import io.geeteshk.rates.model.ExtCurrency
import java.text.NumberFormat
import java.util.*

val supportedCurrencies = arrayOf(
    "HRK", "HUF", "IDR", "PHP", "TRY", "RON", "ISK", "SEK", "THB", "PLN",
    "GBP", "CAD", "AUD", "MYR", "NZD", "CHF", "DKK", "SGD", "CNY", "BGN",
    "CZK", "BRL", "JPY", "KRW", "INR", "MXN", "RUB", "HKD", "USD", "ZAR",
    "ILS", "NOK", "EUR"
)

@DrawableRes fun Currency.flagDrawable(context: Context): Int =
    context.resources.getIdentifier("ic_flag_${currencyCode.toLowerCase()}", "drawable", context.packageName)

fun Double.asCurrency(currency: ExtCurrency): String = this.asCurrency(currency.code)

fun Double.asCurrency(code: String): String {
    val instance = NumberFormat.getCurrencyInstance()
    instance.currency = Currency.getInstance(code)
    return instance.format(this)
}
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

package io.geeteshk.rates.model.db

import androidx.room.TypeConverter

object MapTypeConverter {

    @TypeConverter
    @JvmStatic
    fun mapToString(value: Map<String, Double>?): String? {
        val builder = StringBuilder()
        value?.forEach {
            builder.append(it.key)
            builder.append("=")
            builder.append(it.value)
            builder.append(";")
        } ?: return null

        builder.setLength(builder.length - 1)
        return builder.toString()
    }

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String?): Map<String, Double>? {
        return value?.split(";")?.associate {
            val (key, floatValue) = it.split("=")
            key to floatValue.toDouble()
        }
    }
}
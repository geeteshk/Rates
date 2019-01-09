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

package io.geeteshk.rates.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RatesDao {

    @get:Query("SELECT * FROM rates")
    val all: List<Rates>

    @Query("SELECT * FROM rates WHERE base LIKE :baseCode AND date LIKE :date")
    fun getRates(date: String, baseCode: String): List<Rates>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg rates: Rates)
}
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

package io.geeteshk.rates.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import io.geeteshk.rates.di.DaggerViewModelInjector
import io.geeteshk.rates.di.NetworkModule
import io.geeteshk.rates.di.ViewModelInjector

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {

    private val injector: ViewModelInjector =
        DaggerViewModelInjector.builder().networkModule(NetworkModule).build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is RateListViewModel -> injector.inject(this)
        }
    }
}
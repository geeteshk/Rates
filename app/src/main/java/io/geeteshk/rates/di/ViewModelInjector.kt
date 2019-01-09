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

package io.geeteshk.rates.di

import dagger.Component
import io.geeteshk.rates.viewmodel.RateListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjector {

    fun inject(rateListViewModel: RateListViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}
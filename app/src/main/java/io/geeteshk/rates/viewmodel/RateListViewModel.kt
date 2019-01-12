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
import android.view.View
import androidx.lifecycle.MutableLiveData
import io.geeteshk.rates.R
import io.geeteshk.rates.model.ExtCurrency
import io.geeteshk.rates.model.RateData
import io.geeteshk.rates.model.RatesDao
import io.geeteshk.rates.utils.extensions.asCurrency
import io.geeteshk.rates.network.RateApi
import io.geeteshk.rates.ui.adapter.RateListAdapter
import io.geeteshk.rates.utils.FORMAT_API
import io.geeteshk.rates.utils.extensions.default
import io.geeteshk.rates.utils.extensions.supportedCurrencies
import io.geeteshk.rates.utils.format
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class RateListViewModel(private val ratesDao: RatesDao, private val app: Application) : BaseAndroidViewModel(app) {

    @Inject
    lateinit var rateApi: RateApi

    private val subscription = CompositeDisposable()

    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRates() }

    val dateEndpoint: MutableLiveData<Date> = MutableLiveData<Date>().default(Date())
    val baseCurrency: MutableLiveData<ExtCurrency> = MutableLiveData<ExtCurrency>().default(
        ExtCurrency(app, "USD")
    )

    val currMultiplier: MutableLiveData<Double> = MutableLiveData<Double>().default(1.0)

    val rateListAdapter: RateListAdapter = RateListAdapter(this)

    init {
        loadRates()
    }

    fun loadRates() {
        val date = dateEndpoint.value!!.format(FORMAT_API)
        val base = baseCurrency.value!!.code

        subscription.add(Observable.fromCallable {
            ratesDao.getRates(date, base) }
                .concatMap { dbRateList ->
                    if (dbRateList.isEmpty()) {
                        rateApi.getRates(date, base)
                            .concatMap { apiRates ->
                                ratesDao.insertAll(apiRates)
                                Observable.just(apiRates)
                            }
                    } else {
                        Observable.just(dbRateList[0])
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { TreeMap(it.rates) }
                .map { addMissingEur(it) }
                .map { mapToCurrencyPairList(it) }
                .doOnSubscribe { onStart() }
                .doOnTerminate { onComplete() }
                .subscribe(
                    { onSuccess(it) }, { onError() }
                ))
    }

    private fun addMissingEur(rates: TreeMap<String, Double>): TreeMap<String, Double> {
        if (rates["EUR"] == null) { rates["EUR"] = 1.0 }
        return rates
    }

    private fun mapToCurrencyPairList(rates: TreeMap<String, Double>): List<RateData> {
        val rateList = ArrayList<RateData>()
        rates.forEach {
            val currency = ExtCurrency(app, it.key)
            rateList.add(RateData(
                currency, (it.value * (currMultiplier.value ?: 1.0)).asCurrency(currency), it.value))
        }

        return rateList
    }

    private fun onStart() {
        loadingVisibility.value = true
        errorMessage.value = null
    }

    private fun onComplete() {
        loadingVisibility.value = false
    }

    private fun onSuccess(rateList: List<RateData>) {
        rateListAdapter.updateList(rateList)
    }

    private fun onError() {
        errorMessage.value = R.string.error_loading_rates
        onSuccess(mapToCurrencyPairList(generateFromEmptyMap()))
    }

    private fun generateFromEmptyMap(): TreeMap<String, Double> {
        val map = TreeMap<String, Double>()
        supportedCurrencies.forEach { map[it] = 0.0 }
        return map
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}
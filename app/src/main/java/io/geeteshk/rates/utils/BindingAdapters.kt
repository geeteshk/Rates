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

package io.geeteshk.rates.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import io.geeteshk.rates.model.ExtCurrency
import io.geeteshk.rates.utils.extensions.asCurrency
import io.geeteshk.rates.utils.extensions.getParentActivity
import io.geeteshk.rates.utils.extensions.setRoundedIcon
import io.geeteshk.rates.viewmodel.RateListViewModel
import java.util.*

fun <T> MutableLiveData<T>?.mutableObserver(view: View, observer: (T) -> Unit) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && this != null) {
        observe(parentActivity, Observer {
            observer.invoke(it)
        })
    }
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(progressBar: ContentLoadingProgressBar, visibility: MutableLiveData<Boolean>?) {
    visibility.mutableObserver(progressBar as View) {
        if (it) { progressBar.show() } else { progressBar.hide() }
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    text.mutableObserver(view) { view.text = it }
}

@BindingAdapter("mutableCurrencyValue")
fun setMutableCurrencyValue(view: Chip, viewModel: RateListViewModel) {
    viewModel.currMultiplier.mutableObserver(view) {
        view.text = it.asCurrency(viewModel.baseCurrency.value!!)
        viewModel.baseCurrency.value = viewModel.baseCurrency.value
    }

    viewModel.baseCurrency.mutableObserver(view) { view.text = viewModel.currMultiplier.value!!.asCurrency(it) }
}

@BindingAdapter("mutableChipIcon")
fun setMutableChipIcon(view: Chip, currency: MutableLiveData<ExtCurrency>?) {
    currency.mutableObserver(view) { view.setRoundedIcon(it.flagResource) }
}

@BindingAdapter("mutableYear")
fun setMutableYear(view: TextView, date: MutableLiveData<Date>?) {
    date.mutableObserver(view) {
        val calendar = GregorianCalendar()
        calendar.time = it

        view.text = calendar.get(Calendar.YEAR).toString()
    }
}

@BindingAdapter("mutableFriendlyDate")
fun setMutableFriendlyDate(view: TextView, date: MutableLiveData<Date>?) {
    date.mutableObserver(view) { view.text = it.toFriendlyString() }
}


@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    if (!adapter.hasStableIds()) adapter.setHasStableIds(true)

    view.setHasFixedSize(true)
    view.layoutManager = LinearLayoutManager(view.context)
    view.adapter = adapter
}
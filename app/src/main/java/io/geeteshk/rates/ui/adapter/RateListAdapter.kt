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

package io.geeteshk.rates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.geeteshk.rates.R
import io.geeteshk.rates.databinding.ItemRateBinding
import io.geeteshk.rates.model.RateData
import io.geeteshk.rates.viewmodel.RateListViewModel
import io.geeteshk.rates.viewmodel.RateViewModel

class RateListAdapter(private val rateListViewModel: RateListViewModel) : RecyclerView.Adapter<RateListAdapter.ViewHolder>() {

    private lateinit var exchangeList: List<RateData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRateBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_rate, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exchangeList[position])
    }

    override fun getItemCount(): Int = if (::exchangeList.isInitialized) exchangeList.size else 0

    fun updateList(rateList: List<RateData>) {
        exchangeList = rateList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return exchangeList[position].currency.code.hashCode().toLong()
    }

    inner class ViewHolder(private val binding: ItemRateBinding) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = RateViewModel()

        fun bind(data: RateData) {
            viewModel.bind(
                data.output,
                "1 %s = %.2f %s".format(rateListViewModel.baseCurrency.value?.code, data.rate, data.currency.code)
            )

            binding.viewModel = viewModel
            binding.root.setOnClickListener { rateListViewModel.baseCurrency.value = data.currency }

            binding.flagView.setImageResource(data.currency.flagResource)
            binding.rateCode.text = data.currency.code
            binding.rateCodeDesc.text = data.currency.displayName
        }
    }
}
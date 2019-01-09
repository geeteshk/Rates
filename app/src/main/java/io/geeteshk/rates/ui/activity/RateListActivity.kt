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

package io.geeteshk.rates.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.geeteshk.rates.R
import io.geeteshk.rates.databinding.ActivityRateListBinding
import io.geeteshk.rates.viewmodel.RateListViewModel
import io.geeteshk.rates.viewmodel.ViewModelFactory
import java.util.*

class RateListActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityRateListBinding
    private lateinit var viewModel: RateListViewModel

    private var errorSnackbar: Snackbar? = null

    private val loadRateObserver = Observer<Any> { viewModel.loadRates() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_rate_list)
        setSupportActionBar(binding.toolbar)

        binding.rateList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(RateListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer {
            if (it != null) {
                showError(it)
            } else {
                hideError()
            }
        })

        viewModel.dateEndpoint.observe(this, loadRateObserver)
        viewModel.baseCurrency.observe(this, loadRateObserver)

        binding.baseChip.setOnClickListener {
            showMultiplierInputDialog()
        }

        binding.viewModel = viewModel
    }

    private fun showMultiplierInputDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Base Amount")


        val input = TextInputEditText(this)
        input.setSingleLine(true)
        input.inputType = InputType.TYPE_CLASS_NUMBER or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        input.setText(viewModel.currMultiplier.value!!.toString())

        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = resources.getDimensionPixelSize(R.dimen.dialog_input_margin)

        params.leftMargin = margin
        params.rightMargin = margin
        params.marginStart = margin
        params.marginEnd = margin

        input.layoutParams = params
        container.addView(input)

        builder.setView(container)
            .setPositiveButton("UPDATE") { _: DialogInterface, _: Int ->
                viewModel.currMultiplier.value = input.text.toString().toDouble()
            }
            .setNegativeButton("CANCEL", null)

        builder.show()
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.action_retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rate_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_choose_date -> {
                val currentDate = viewModel.dateEndpoint.value
                val calendar = GregorianCalendar()
                calendar.time = currentDate

                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(this, this, currentYear, currentMonth, currentDay).show()
            }

            R.id.action_notices -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            }

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar(year, month, dayOfMonth)
        val oldest = GregorianCalendar(1999, 1, 4)
        when {
            calendar.time.after(Date()) -> viewModel.dateEndpoint.value = Date()
            calendar.time.before(oldest.time) -> viewModel.dateEndpoint.value = oldest.time
            else -> viewModel.dateEndpoint.value = calendar.time
        }
    }
}

package com.hisab.kheti.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.*
import com.hisab.kheti.utils.convertDobToAppFormatDate
import kotlinx.android.synthetic.main.frament_add_update_transaction_list.*
import kotlinx.android.synthetic.main.frament_add_update_transaction_list.view.*
import java.util.*


class AddUpdateTransactionFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    var selectedCatName = ""
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

        if (p1) {
            if (p0?.id == R.id.rb_expense) {
                categoryList.clear()
                categoryList.addAll(App.database.getDataDao().getCategory(CategoryType.EXPENSES.type))
                adapter = ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, categoryList.map { it.categoryName })
                sp_category.adapter = adapter
                sp_category.isEnabled = true

            } else if (p0?.id == R.id.rb_income) {
                categoryList.clear()
                categoryList.addAll(App.database.getDataDao().getCategory(CategoryType.INCOME.type))
                adapter = ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, categoryList.map { it.categoryName })
                sp_category.adapter = adapter
                sp_category.isEnabled = true
            }
            sp_category.setSelection(categoryList.indexOfFirst { it.categoryName.equals(selectedCatName, true) })
        }
    }

    private var cropId: String? = null
    private var transactionId: String? = null
    private var transaction: Transaction? = null
    private var startDate: Date? = null
    private var categoryList: MutableList<Category> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_add_update_transaction_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        extractArguments()
        setupListener()
        loadData()
        super.onActivityCreated(savedInstanceState)
    }

    private fun extractArguments() {
        cropId = arguments?.getString("CROP_ID", null)
        startDate = Date()
        transactionId = arguments?.getString("TRANSACTION_ID", null)
        tv_date.text = startDate?.convertDobToAppFormatDate()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_save_transaction -> {
                saveData()
            }

            R.id.tv_date -> {
                openDatePickerDialog()
            }
        }
    }

    private fun saveData() {


        if (cropId != null) {

            if (TextUtils.isEmpty(et_amount.text)) {
                AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.str_amount_issue))
                        .setPositiveButton(getString(android.R.string.ok)) { p0, p1 -> p0?.dismiss() }
                        .show()
                return
            }

            if (TextUtils.isEmpty(et_note.text)) {
                AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.str_note_issue))
                        .setPositiveButton(getString(android.R.string.ok)) { p0, p1 -> p0?.dismiss() }
                        .show()
                return
            }


            if (transactionId != null) {
                transaction?.watcher?.updatedAt = Date()
                transaction?.transactionAmount = et_amount.text.toString()
                transaction?.transactionNote = et_note.text.toString()
                transaction?.transactionNote = et_note.text.toString()
                transaction?.categoryId = categoryList.find { it.categoryName.equals(sp_category.selectedItem.toString(), true) }!!.categoryId
                transaction?.transactionStatus = getStatus()

                App.database.getInsertUpdateDao().insert(transaction!!)
            } else {
                val watcher = Watcher(Date(), null, null)
                val transaction = Transaction(UUID.randomUUID().toString(),
                        cropId!!,
                        categoryList.find { it.categoryName.equals(sp_category.selectedItem.toString(), true) }!!.categoryId,
                        et_amount.text.toString(),
                        getStatus(),
                        Date(),
                        et_note.text.toString(),
                        watcher
                )
                App.database.getInsertUpdateDao().insert(transaction)
            }
            activity?.onBackPressed()
        }

    }

    private fun loadData() {
        transactionId?.let {
            transaction = App.database.getDataDao().getTransactionsById(transactionId)


            if (transaction != null) {
                et_amount.setText(transaction?.transactionAmount)
                et_note.setText(transaction?.transactionNote)
                startDate = transaction?.transactionDate
                tv_date.text = transaction?.transactionDate?.convertDobToAppFormatDate()
            }

            if (TransactionType.UNPAID.type.equals(transaction?.transactionStatus)) {
                rb_unpaid.isChecked = true
            } else {
                rb_paid.isChecked = true
            }

            val cat = App.database.getDataDao().getCategoryById(transaction?.categoryId!!)
            selectedCatName = cat.categoryName
            if (CategoryType.INCOME.type.equals(cat.transactionType)) {
                rb_income.isChecked = true
            } else {
                rb_expense.isChecked = true
            }
        }

    }

    private fun setupListener() {
        btn_save_transaction.setOnClickListener(this)
        tv_date.setOnClickListener(this)
        rb_expense.setOnCheckedChangeListener(this)
        rb_income.setOnCheckedChangeListener(this)
        rb_expense.isChecked = true
        rb_paid.isChecked = true
    }

    private fun getStatus(): String {
        return if (rb_paid.isChecked) {
            TransactionType.PAID.type
        } else {
            TransactionType.UNPAID.type
        }
    }

    private fun openDatePickerDialog() {

        val cal = Calendar.getInstance()

        val dateDialog = DatePickerDialog(context, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            tv_date.setText("$day/${month + 1}/$year")
            startDate = Date(year, month, day)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))

        dateDialog.show()
    }
}


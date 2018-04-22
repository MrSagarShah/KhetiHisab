package com.hisab.kheti.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.Category
import com.hisab.kheti.data.CategoryType
import com.hisab.kheti.data.Watcher
import kotlinx.android.synthetic.main.frament_add_update_category.*
import java.util.*

class AddUpdateCategoryFragment : Fragment(), View.OnClickListener {

    private var categoryId: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_add_update_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        extractArguments()
        setData()
        setupListener()
        super.onActivityCreated(savedInstanceState)
    }

    private fun extractArguments() {
        categoryId = arguments?.getString("CATEGORY_ID", null)
    }

    private fun setData(){
        categoryId?.let {
            val cat = App.database.getDataDao().getCategoryById(it)
            tvCategoryName.setText(cat.categoryName)
            if(cat.transactionType.equals(CategoryType.INCOME.type)){
                rb_income.isChecked = true
            } else{
                rb_expense.isChecked = true
            }

        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSave -> {
                if (tvCategoryName.text.isNullOrBlank()) {
                    Toast.makeText(context, "Category Name can not be empty.", Toast.LENGTH_SHORT).show()
                    return
                }
                val category = Category(categoryId ?: UUID.randomUUID().toString(),
                        tvCategoryName.text.toString(),
                        "",
                        if (rb_expense.isChecked) CategoryType.EXPENSES.type else CategoryType.INCOME.type,
                        Watcher(Date(), null, null))

                App.database.getInsertUpdateDao().insert(category)
                activity?.onBackPressed()
            }
        }
    }

    private fun setupListener() {
        btnSave.setOnClickListener(this)
    }

}


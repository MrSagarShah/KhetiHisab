package com.hisab.kheti.ui

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.Category
import com.hisab.kheti.data.CategoryType
import com.hisab.kheti.utils.addFragmentWithBackstack
import kotlinx.android.synthetic.main.frament_category_list.*
import kotlinx.android.synthetic.main.row_category_item.view.*

class CategoryListFragment : Fragment(), View.OnClickListener, OnCategorySelected {
    private var adapter: CategoryAdapter? = null

    private var categoryList: MutableList<Category> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_category_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupListener()
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        loadData()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_add_category -> {
                // TODO add the add update category fragment
            }
        }
    }

    override fun itemClicked(data: Category) {
        val bundle = Bundle()
        bundle.putString("CATEGORY_ID", data.categoryId)
        // TODO add the add update category fragment
    }

    private fun setupListener() {
        btn_add_category.setOnClickListener(this)
    }

    private fun loadData() {
        App.database.getDataDao().getAllCategory().observe(activity!!, Observer<List<Category>> { t ->
            if (t != null) {
                categoryList.clear()
                categoryList.addAll(t)
            }
            if (adapter != null) {
                adapter?.notifyDataSetChanged()
            } else {
                adapter = CategoryAdapter(categoryList, this)
                recycler_view.adapter = adapter
            }
        })
    }
}

class CategoryAdapter(private val categoryList: MutableList<Category>, private val listener: OnCategorySelected) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_category_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindViews(categoryList[holder.adapterPosition])
        holder?.itemView?.setOnClickListener({
            listener.itemClicked(categoryList[holder.adapterPosition])
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(data: Category) {
            itemView?.tv_category?.text = data.categoryName
            itemView?.tv_category?.setTextColor(if (data.transactionType.equals(CategoryType.EXPENSES.type)) Color.RED else Color.GREEN)
        }
    }
}

interface OnCategorySelected {
    fun itemClicked(data: Category)
}

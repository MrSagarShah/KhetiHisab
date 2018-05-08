package com.hisab.kheti.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.CropModel
import com.hisab.kheti.utils.addFragmentWithBackstack
import kotlinx.android.synthetic.main.frament_crop_list.*
import kotlinx.android.synthetic.main.row_crop_item.view.*

class CropListFragment : Fragment(), View.OnClickListener, OnCropSelected {
    private var adapter: CropAdapter? = null

    private var cropList: MutableList<CropModel> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_crop_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupListener()
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, CropListFragment::class.java.canonicalName)
        FirebaseAnalytics.getInstance(activity).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)
        recycler_view.addItemDecoration(itemDecoration)
        loadData()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_add_crop -> {
                (activity as AppCompatActivity).addFragmentWithBackstack(AddCropFragment(), R.id.fragment_container)
            }
        }
    }

    override fun itemClicked(data: CropModel) {
        val bundle = Bundle()
        bundle.putString("CROP_ID", data.crop_id)
        (activity as AppCompatActivity).addFragmentWithBackstack(TransactionFragment(), R.id.fragment_container, bundle)
    }

    private fun setupListener() {
        btn_add_crop.setOnClickListener(this)
    }

    private fun loadData() {
        App.database.getDataDao().getCropWithData().observe(activity!!, Observer<MutableList<CropModel>> { t ->
            if (t != null) {
                cropList.clear()
                cropList.addAll(t)
            }
            if (adapter != null) {
                adapter?.notifyDataSetChanged()
            } else {
                adapter = CropAdapter(cropList, this)
                recycler_view.adapter = adapter
            }
        })
    }
}

class CropAdapter(private val cropList: MutableList<CropModel>, private val listener: OnCropSelected) : RecyclerView.Adapter<CropAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_crop_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cropList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindViews(cropList[holder.adapterPosition])
        holder?.itemView?.setOnClickListener({
            listener.itemClicked(cropList[holder.adapterPosition])
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(data: CropModel) {
            itemView?.tv_crop?.text = data.crop_name
            itemView?.tv_expense_amount?.text = (data.expense
                    ?: 0).toString()
            itemView?.tv_income_amount?.text = (data.income
                    ?: 0).toString()
            itemView?.tv_pnl_amount?.text =(data.income?.toInt()?:0.minus(data.expense?.toInt()?:0)).toString()
        }
    }
}

interface OnCropSelected {
    fun itemClicked(data: CropModel)
}
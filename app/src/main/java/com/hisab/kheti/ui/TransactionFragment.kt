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
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.TransactionData
import com.hisab.kheti.utils.addFragmentWithBackstack
import com.hisab.kheti.utils.convertDobToAppFormatDate
import kotlinx.android.synthetic.main.frament_transaction_list.*
import kotlinx.android.synthetic.main.row_transaction_item.view.*

class TransactionFragment : Fragment(), View.OnClickListener, OnTransactionClickEvent {

    private var adapter: TransactionAdapter? = null
    private var transactionList: MutableList<TransactionData> = mutableListOf()
    private var cropId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_transaction_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        extractArguments()
        setupRecyclerView()
        setupListener()
        super.onActivityCreated(savedInstanceState)
    }

    private fun extractArguments() {
        cropId = arguments?.getString("CROP_ID", null)
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

                val bundle = Bundle()
                bundle.putString("CROP_ID", cropId)
                (activity as AppCompatActivity).addFragmentWithBackstack(AddUpdateTransactionFragment(), R.id.fragment_container, bundle)
            }
        }
    }

    override fun itemClicked(transactionData: TransactionData) {
        val bundle = Bundle()
        bundle.putString("TRANSACTION_ID", transactionData.id)
        bundle.putString("CROP_ID", cropId)
        (activity as AppCompatActivity).addFragmentWithBackstack(AddUpdateTransactionFragment(), R.id.fragment_container, bundle)
    }


    private fun loadData() {

        App.database.getDataDao().getTransactionsNew(cropId)
                .observe(activity!!, Observer<MutableList<TransactionData>> { t ->
                    if (t != null) {
                        transactionList.clear()
                        transactionList.addAll(t)
                    }
                    if (adapter != null) {
                        adapter?.notifyDataSetChanged()
                    } else {
                        adapter = TransactionAdapter(transactionList, this)
                        recycler_view.adapter = adapter
                    }
                })
    }

    private fun setupListener() {
        btn_add_crop.setOnClickListener(this)
    }

}

class TransactionAdapter(private val transactionList: MutableList<TransactionData>,
                         val listener: OnTransactionClickEvent) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_transaction_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindViews(transactionList[holder.adapterPosition])
        holder?.itemView?.setOnClickListener {
            listener.itemClicked(transactionList[holder.adapterPosition])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(data: TransactionData) {
            itemView?.tv_crop?.text = data.category_name
            itemView?.tv_amount?.text = data.amount
            itemView?.tv_status?.text = data.status
            itemView?.tv_notes?.text = data.notes
            itemView?.tv_date?.text = data.date?.convertDobToAppFormatDate()
        }
    }
}

interface OnTransactionClickEvent {
    fun itemClicked(transactionData: TransactionData)
}

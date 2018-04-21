package com.hisab.kheti.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.Crop
import com.hisab.kheti.data.Watcher
import kotlinx.android.synthetic.main.frament_add_crop.*
import java.util.*

class AddCropFragment : Fragment(), View.OnClickListener {

    var startDate: Date? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frament_add_crop, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupListener()
        super.onActivityCreated(savedInstanceState)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_add_crop -> {
                addCropInDb()
            }
            R.id.tv_date -> {
                openDatePickerDialog()
            }
        }
    }

    private fun addCropInDb() {
        val watcher = Watcher(Date(), null, null)
        val id = UUID.randomUUID().toString()
        val crop = Crop(id, et_name.text.toString(), "", startDate, null, watcher)
        App.database.getInsertUpdateDao().insert(crop)
        activity?.onBackPressed()
    }

    private fun setupListener() {
        btn_add_crop.setOnClickListener(this)
        tv_date.setOnClickListener(this)
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

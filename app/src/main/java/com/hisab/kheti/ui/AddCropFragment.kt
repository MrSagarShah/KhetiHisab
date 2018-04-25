package com.hisab.kheti.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.hisab.kheti.App
import com.hisab.kheti.R
import com.hisab.kheti.data.Crop
import com.hisab.kheti.data.Watcher
import kotlinx.android.synthetic.main.frament_add_crop.*
import java.util.*

class AddCropFragment : Fragment(), View.OnClickListener {

    private var startDate: Date? = null
    private var completionDate: Date? = null
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
                openDatePickerDialog(tv_date)
            }
            R.id.tvCompletionDate -> {
                openDatePickerDialog(tvCompletionDate)
            }
        }
    }

    private fun addCropInDb() {

        if (et_name.text.isNullOrBlank()) {
            Toast.makeText(activity, "Please enter crop name", Toast.LENGTH_SHORT).show()
            return
        }
        if (etFarmerName.text.isNullOrBlank()) {
            Toast.makeText(activity, "Please enter farmer name", Toast.LENGTH_SHORT).show()
            return
        }
        if (etLandUsed.text.isNullOrBlank()) {
            Toast.makeText(activity, "Please enter Land Used for Crop", Toast.LENGTH_SHORT).show()
            return
        }
        if (startDate == null) {
            Toast.makeText(activity, "Please select the start date", Toast.LENGTH_SHORT).show()
            return
        }

        val watcher = Watcher(Date(), null, null)
        val id = UUID.randomUUID().toString()
        val crop = Crop(id, et_name.text.toString(), etFarmerName.text.toString(), etLandUsed.text.toString(), startDate, completionDate, watcher)
        App.database.getInsertUpdateDao().insert(crop)
        activity?.onBackPressed()
    }

    private fun setupListener() {
        btn_add_crop.setOnClickListener(this)
        tv_date.setOnClickListener(this)
    }

    private fun openDatePickerDialog(view: TextView) {

        val cal = Calendar.getInstance()

        val dateDialog = DatePickerDialog(context, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            view.setText("$day/${month + 1}/$year")
            if (view.id == R.id.tvCompletionDate) {
                completionDate = Date(year, month, day)
            } else {
                startDate = Date(year, month, day)
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))

        dateDialog.show()
    }

}

package com.hisab.kheti.data

import java.util.*

/**
 * Created by Hp on 04-03-2018.
 */
data class TransactionData(
        val id: String?,
        val status: String?,
        val amount: String?,
        val notes: String?,
        val date: Date?,
        val category_name: String?,
        val category_image: String?
)

data class CropModel(
        val crop_id: String,
        val crop_name: String,
        val expense: String?,
        val income: String?
)
package com.hisab.kheti.data

import android.arch.persistence.room.*
import android.text.TextUtils
import com.hisab.kheti.Constant.COMMON_DATE_FORMAT
import com.hisab.kheti.Constant.DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class StringDateConverters {
    private val datetime = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val date = SimpleDateFormat(COMMON_DATE_FORMAT, Locale.getDefault())

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        if (!TextUtils.isEmpty(value)) {
            try {
                return datetime.parse(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                return date.parse(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Date()
        } else {
            return Date()
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return if (date == null) null else datetime.format(Date(sdf.format(date)))
    }
}

class Watcher(
        @ColumnInfo(name = "created_at")
        var createdAt: Date?,
        @ColumnInfo(name = "updated_at")
        var updatedAt: Date?,
        @ColumnInfo(name = "deleted_at")
        var deletedAt: Date?,
        var status: Int = 1
)

@Entity(tableName = "kh_crop")
data class Crop(
        @PrimaryKey
        @ColumnInfo(name = "crop_id")
        val cropId: String,
        @ColumnInfo(name = "crop_name")
        val cropName: String,
        @ColumnInfo(name = "crop_image")
        val cropImage: String,
        @ColumnInfo(name = "crop_start_date")
        val cropStartDate: Date?,
        @ColumnInfo(name = "crop_end_date")
        val cropEndDate: Date?,
        @Embedded
        val watcher: Watcher
)

@Entity(tableName = "kh_category")
data class Category(
        @PrimaryKey
        @ColumnInfo(name = "category_id")
        val categoryId: String,
        @ColumnInfo(name = "category_name")
        val categoryName: String,
        @ColumnInfo(name = "category_image")
        val categoryImage: String,
        @ColumnInfo(name = "category_type")
        var transactionType: String,
        @ColumnInfo(name = "crop_start_date")
        val cropStartDate: Date?,
        @ColumnInfo(name = "crop_end_date")
        val cropEndDate: Date?,
        @Embedded
        val watcher: Watcher
)

@Entity(tableName = "kh_transaction")
data class Transaction(
        @PrimaryKey
        @ColumnInfo(name = "transaction_id")
        var transactionId: String,
        @ColumnInfo(name = "crop_id")
        var cropId: String,
        @ColumnInfo(name = "category_id")
        var categoryId: String,
        @ColumnInfo(name = "transaction_amount")
        var transactionAmount: String,
        @ColumnInfo(name = "transaction_status")
        var transactionStatus: String,
        @ColumnInfo(name = "transaction_date")
        var transactionDate: Date,
        @ColumnInfo(name = "transaction_note")
        var transactionNote: String?,
        @Embedded
        var watcher: Watcher
)

enum class CategoryEnum(val type: String) {
    SEED("SEED"),
    TRACTOR("TRACTOR"),
    FERTILIZER("FERTILIZER"),
    MEDICINE("MEDICINE"),
    LIGHT_BILL("LIGHT_BILL"),
    EQUIPMENTS("EQUIPMENTS"),
    CUTTER("CUTTER"),
    FARMER_TAKE("FARMER_TAKE"),
    FARMER_EXPENSES("FARMER_EXPENSES"),
    EXTRA("EXTRA"),
    SELL("SELL")
}

enum class CategoryType(val type: String) {
    EXPENSES("EXPENSES"),
    INCOME("INCOME")
}


enum class TransactionType(val type: String) {
    PAID("PAID"),
    UNPAID("UNPAID")
}
package com.hisab.kheti.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/**
 * Created by Hp on 20-02-2018.
 */

@Dao
interface InsertUpdateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(test: Crop)

    @Update
    fun update(test: Crop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(test: Category)

    @Update
    fun update(test: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(test: Transaction)

    @Update
    fun update(test: Transaction)
}


@Dao
interface GetDataDao {
    @Query("select * from kh_crop")
    fun getCrops(): LiveData<MutableList<Crop>>

    @Query("select crop_id, crop_name, (select sum(kh_transaction.transaction_amount) \n" +
            "from \n" +
            "kh_transaction, kh_category \n" +
            "where kh_transaction.crop_id = crop.crop_id and kh_transaction.category_id = kh_category.category_id and kh_category.category_type =\"EXPENSES\"\n" +
            ") as expense,\n" +
            "(select sum(kh_transaction.transaction_amount) \n" +
            "from \n" +
            "kh_transaction, kh_category \n" +
            "where kh_transaction.crop_id = crop.crop_id and kh_transaction.category_id = kh_category.category_id and kh_category.category_type =\"INCOME\"\n" +
            ") as income   from kh_crop as crop")
    fun getCropWithData(): LiveData<MutableList<CropModel>>

    @Query("select * from kh_category where category_type = :type")
    fun getCategory(type : String): List<Category>

    @Query("select * from kh_category")
    fun getAllCategory(): LiveData<List<Category>>

    @Query("select * from kh_category where category_id = :id")
    fun getCategoryById(id : String): Category


    @Query("select * from kh_transaction where transaction_id = :transactionId")
    fun getTransactionsById(transactionId: String?): Transaction

    @Query("select transaction_id as id,transaction_status as status, transaction_amount as amount,\n" +
            "transaction_note as notes, transaction_date as date, category_name, category_image, category_type as type\n" +
            "from kh_transaction,kh_category where crop_id = :cropId and kh_transaction.category_id = kh_category.category_id")
    fun getTransactionsNew(cropId: String?): LiveData<MutableList<TransactionData>>
}
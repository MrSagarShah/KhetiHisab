package com.hisab.kheti

import android.app.Application
import android.arch.persistence.room.Room
import com.hisab.kheti.data.*
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        App.Companion.database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()


        val watcher = Watcher(Date(), Date(), Date())
        App.database.getInsertUpdateDao().insert(Crop("1", "weath", "ABC", "2 vigha", Date(), Date(), watcher))

        App.database.getInsertUpdateDao().insert(Category("1", CategoryEnum.CUTTER.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("2", CategoryEnum.SEED.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("3", CategoryEnum.TRACTOR.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("4", CategoryEnum.EQUIPMENTS.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("5", CategoryEnum.EXTRA.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("6", CategoryEnum.FARMER_EXPENSES.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("7", CategoryEnum.FERTILIZER.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("8", CategoryEnum.LIGHT_BILL.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("9", CategoryEnum.MEDICINE.name, "",CategoryType.EXPENSES.type, watcher))
        App.database.getInsertUpdateDao().insert(Category("10", CategoryEnum.SELL.name, "",CategoryType.INCOME.type, watcher))

        App.database.getInsertUpdateDao().insert(Transaction("1", "1", "1", "3000", TransactionType.PAID.type, Date(), "Cutter notes", watcher))
        App.database.getInsertUpdateDao().insert(Transaction("2", "1", "1", "3000", TransactionType.PAID.type, Date(), "Cutter notes", watcher))
    }

    companion object {
        lateinit var database: AppDatabase
    }
}
package com.cjhub.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.cjhub.data.dao.AccountDaoImpl
import com.cjhub.data.dao.CategoryDaoImpl
import com.cjhub.data.dao.CurrencyDaoImpl
import com.cjhub.data.dao.TransactionDaoImpl
import com.cjhub.data.entities.AccountEntity
import com.cjhub.data.entities.CategoryEntity
import com.cjhub.data.entities.CurrencyEntity
import com.cjhub.data.entities.TransactionEntity

import java.util.concurrent.Executors

/**
 * Room implementation of My Wallet database.
 */
@Database(
    entities = [
        CurrencyEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class MyWalletDatabase : RoomDatabase() {

    companion object {

        private var INSTANCE: MyWalletDatabase? = null

        @Synchronized
        private fun getInstance(context: Context): MyWalletDatabase {
            return INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): MyWalletDatabase {
            return Room.databaseBuilder(context, MyWalletDatabase::class.java, "my_wallet_db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            Executors.newSingleThreadExecutor().execute {
                                listOf(
                                    CurrencyEntity.DOLLAR,
                                    CurrencyEntity.EURO,
                                    CurrencyEntity.POUND,
                                    CurrencyEntity.BAHT
                                ).forEach { currency ->
                                    getInstance(context).currencyDao().insertOrUpdate(currency)
                                }
                                listOf(
                                    CategoryEntity.OTHER_INCOME,
                                    CategoryEntity.OTHER_EXPENSE,
                                    CategoryEntity.TRANSFER
                                ).forEach { category ->
                                    getInstance(context).categoryDao().insertOrUpdate(category)
                                }
                                listOf(
                                    AccountEntity.MY_WALLET
                                ).forEach { account ->
                                    getInstance(context).accountDao().insertOfUpdate(account)
                                }
                            }
                        }
                    })
                    .build()
        }

        fun getCurrencyDao(context: Context): CurrencyDaoImpl {
            return getInstance(context).currencyDao()
        }

        fun getTransactionDao(context: Context): TransactionDaoImpl {
            return getInstance(context).transactionDao()
        }

        fun getCategoryDao(context: Context): CategoryDaoImpl {
            return getInstance(context).categoryDao()
        }

        fun getAccountDao(context: Context): AccountDaoImpl {
            return getInstance(context).accountDao()
        }
    }

    abstract fun currencyDao(): CurrencyDaoImpl

    abstract fun transactionDao(): TransactionDaoImpl

    abstract fun categoryDao(): CategoryDaoImpl

    abstract fun accountDao(): AccountDaoImpl
}

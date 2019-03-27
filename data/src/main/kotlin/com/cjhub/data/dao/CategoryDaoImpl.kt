package com.cjhub.data.dao

import io.reactivex.Single

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.cjhub.domain.contracts.dao.CategoryDao

import com.cjhub.data.entities.CategoryEntity

/**
 * Data access object for Category entity.
 */
@Dao
interface CategoryDaoImpl : CategoryDao {

    @Query("SELECT * FROM categories;")
    fun getAll(): Single<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE type = :type;")
    fun getAllByType(type: String): Single<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(category: CategoryEntity)

    @Delete
    fun delete(category: CategoryEntity)

    @Query("UPDATE categories SET total = 0.0;")
    fun reset()
}

package com.lilly.codingchallenge.module

import android.content.Context
import androidx.room.Room
import com.lilly.codingchallenge.data.database.ProductDatabase
import com.lilly.codingchallenge.data.database.dao.CartItemsDAO
import com.lilly.codingchallenge.data.database.dao.OrderSummaryDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ProductDatabase {
        return Room.databaseBuilder(
            appContext,
            ProductDatabase::class.java,
            "ProductDatabase.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartItemsDAO(productDatabase: ProductDatabase) : CartItemsDAO {
        return productDatabase.cartItemDAO()
    }

    @Provides
    @Singleton
    fun provideOrderSummaryDAO(productDatabase: ProductDatabase) : OrderSummaryDAO {
        return productDatabase.orderSummaryDAO()
    }
}
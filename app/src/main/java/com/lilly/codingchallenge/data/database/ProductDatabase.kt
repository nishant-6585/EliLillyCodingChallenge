package com.lilly.codingchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lilly.codingchallenge.data.database.dao.CartItemsDAO
import com.lilly.codingchallenge.data.database.dao.OrderSummaryDAO
import com.lilly.codingchallenge.data.database.entity.CartItems
import com.lilly.codingchallenge.data.database.entity.OrderSummary

@Database(
    entities = [CartItems::class, OrderSummary::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun cartItemDAO(): CartItemsDAO
    abstract fun orderSummaryDAO(): OrderSummaryDAO

}
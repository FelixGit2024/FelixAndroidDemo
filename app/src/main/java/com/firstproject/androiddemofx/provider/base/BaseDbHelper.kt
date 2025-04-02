package com.firstproject.androiddemofx.provider.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.firstproject.androiddemofx.provider.DatabaseInfo

abstract class BaseDbHelper(context: Context) :
    SQLiteOpenHelper(context, DatabaseInfo.DATABASE, null, DatabaseInfo.VERSION) {

    abstract fun getTableName(): String
    abstract fun buildCreateTableSQL(): String

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(buildCreateTableSQL())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${getTableName()}")
        onCreate(db)
    }
}
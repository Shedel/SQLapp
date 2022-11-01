package com.example.sqlapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import androidx.core.database.getIntOrNull
import androidx.core.graphics.createBitmap

class SQLiteHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "item.db"
        private const val TBL_ITEM = "tbl_item"
        private const val ID = "id"
        private const val NAME = "name"
        private const val FACTORY = "factory"
        private const val NUMBER = "number"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblItem = (" CREATE TABLE " + TBL_ITEM +" ( "
                + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, "
                + FACTORY + " TEXT, " + NUMBER + " TEXT " + " ) ")
        db?.execSQL(createTblItem)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_ITEM")
        onCreate(db)
    }
    fun insertItem(std: ItemModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(FACTORY, std.factory)
        contentValues.put(NUMBER, std.number)

        val success = db.insert(TBL_ITEM, null, contentValues)
        db.close()
        return success
    }

    fun getALLITEM(): ArrayList<ItemModel>{
        val stdList: ArrayList<ItemModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_ITEM"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var factory: String
        var number: String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                factory = cursor.getString(cursor.getColumnIndex("factory"))
                number = cursor.getString(cursor.getColumnIndex("number"))

                val std = ItemModel(id = id, name = name, factory = factory, number = number)
                stdList.add(std)
            }while (cursor.moveToNext())
        }
        return stdList
    }
    fun updateItem(std: ItemModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(FACTORY, std.factory)
        contentValues.put(NUMBER, std.number)

        val success = db.update(TBL_ITEM, contentValues, "id=" + std.id, null)
        db.close()
        return success
    }

    fun deleteItemById(id:Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        ContentValues.put(ID, id)

        val success = db.delete(TBL_ITEM, "id=$id", null)
        db.close()
        return success
    }
}
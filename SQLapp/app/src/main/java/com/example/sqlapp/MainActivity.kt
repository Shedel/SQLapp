package com.example.sqlapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edFactory: EditText
    private lateinit var edNumber: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter:ItemAdapter? = null
    private var std:ItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addItem() }
        btnView.setOnClickListener{ getItem() }
        btnUpdate.setOnClickListener{ updateItem() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edFactory.setText(it.factory)
            edNumber.setText(it.number)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteItem(it.id)
        }
    }

    private fun getItem() {
        val stdList = sqliteHelper.getALLITEM()
        Log.e("pppp", "${stdList.size}" )
        adapter?.addItems(stdList)
    }

    private fun addItem() {
        val name = edName.text.toString()
        val factory = edFactory.text.toString()
        val number = edNumber.text.toString()

        if (name.isEmpty() || factory.isEmpty() || number.isEmpty()) {
            Toast.makeText(this, "Please enter required field ", Toast.LENGTH_SHORT).show()
        } else {
            val std = ItemModel(name = name, factory = factory, number = number)
            val status = sqliteHelper.insertItem(std)
            //Проверка ввода успешна или нет
            if (status > -1) {
                Toast.makeText(this, "Item Added...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getItem()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateItem(){
        val name = edName.text.toString()
        val factory = edFactory.text.toString()
        val number = edNumber.text.toString()

        if(name == std?.name && factory == std?.factory && number == std?.number) {
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return
        }

        if(std == null) return
            val std = ItemModel(id = std!!.id, name = name, factory = factory, number = number)
            val status = sqliteHelper.updateItem(std)
        if (status > -1) {
            clearEditText()
            getItem()
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteItem(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, _ ->
            sqliteHelper.deleteItemById(id)
            getItem()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun clearEditText() {
        edName.setText("")
        edFactory.setText("")
        edNumber.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView(){
        recyclerView.LayoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edFactory = findViewById(R.id.edFactory)
        edNumber = findViewById(R.id.edNumber)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)


    }
}
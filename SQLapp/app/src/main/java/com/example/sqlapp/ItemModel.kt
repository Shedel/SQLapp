package com.example.sqlapp

import java.util.*

class ItemModel(
    var id: Int = getAutoId(),
    var name: String = "",
    var factory: String = "",
    var number: String = "",
) {
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)

        }
    }
}
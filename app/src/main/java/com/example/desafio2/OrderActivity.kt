package com.example.desafio2
import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        loadOrders()

        binding.btnConfirmOrder.setOnClickListener {
            confirmOrder()
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun loadOrders() {
        val cursor = dbHelper.getOrders()
        val orderItems = mutableListOf<String>()
        var total = 0.0

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY))

            val itemTotal = price + quantity
            total += itemTotal

            orderItems.add("$name x$quantity - $${String.format("%.2f", itemTotal)}")
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderItems)
        binding.orderListView.adapter = adapter

        binding.tvTotal.text = "Total: $${String.format("%.2f", total)}"
    }

    private fun confirmOrder() {
        val cursor = dbHelper.getOrders()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY))

            dbHelper.insertHistory(name, price, quantity)
        }
        cursor.close()

        dbHelper.clearOrders()
    }
}
//si
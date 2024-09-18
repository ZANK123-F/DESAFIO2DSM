package com.example.desafio2
import DatabaseHelper
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        loadHistory()
    }

    private fun loadHistory() {
        val cursor = dbHelper.getHistory()
        val historyItems = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("order_time"))

            historyItems.add("$time: $name x$quantity - $${String.format("%.2f", price + quantity)}")
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyItems)
        binding.historyListView.adapter = adapter
    }
}
//si
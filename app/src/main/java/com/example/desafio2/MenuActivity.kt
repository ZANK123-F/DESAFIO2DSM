package com.example.desafio2
import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupRecyclerView()
        loadMenuItems()

        binding.btnViewOrder.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(emptyList()) { item, quantity ->
            dbHelper.insertOrder(item.name, item.price, quantity)
        }
        binding.recyclerMenu.adapter = adapter
        binding.recyclerMenu.layoutManager = LinearLayoutManager(this)
    }

    private fun loadMenuItems() {
        val cursor = dbHelper.getMenuItems()
        val menuItems = mutableListOf<MenuItem>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE))

            menuItems.add(MenuItem(id, name, price, type))
        }
        cursor.close()

        adapter.updateItems(menuItems)
    }
}

data class MenuItem(val id: Int, val name: String, val price: Double, val type: String)

class MenuAdapter(
    private var items: List<MenuItem>,
    private val onItemSelected: (MenuItem, Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.itemName)
        val priceText: TextView = view.findViewById(R.id.itemPrice)
        val quantityText: TextView = view.findViewById(R.id.itemQuantity)
        val addButton: Button = view.findViewById(R.id.btnAdd)
        val removeButton: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.priceText.text = "$${item.price}"

        var quantity = 0
        holder.quantityText.text = quantity.toString()

        holder.addButton.setOnClickListener {
            quantity++
            holder.quantityText.text = quantity.toString()
            onItemSelected(item, quantity)
        }

        holder.removeButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                holder.quantityText.text = quantity.toString()
                onItemSelected(item, quantity)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
//si
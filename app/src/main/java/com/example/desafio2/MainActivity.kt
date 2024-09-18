package com.example.desafio2

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)


        populateDatabase()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Usuario y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isAuthenticated = dbHelper.checkUser(username, password)
            if (isAuthenticated) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MenuActivity::class.java))
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun populateDatabase() {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_MENU, null, null)

        dbHelper.insertMenuItem("Tacos ", 50.99, "food")
        dbHelper.insertMenuItem("Tortas", 4.99, "food")
        dbHelper.insertMenuItem("Quesadillas calientitas", 2.99, "food")
        dbHelper.insertMenuItem("Pollo Picante", 6.99, "food")
        dbHelper.insertMenuItem("Chiles Habaneros Rellenos", 17.99, "food")
        dbHelper.insertMenuItem("Tamales de elote picantes", 4.99, "food")
        dbHelper.insertMenuItem("Tamales pizques", 1.99, "food")
        dbHelper.insertMenuItem("Coca Cola", 2.50, "drink")
        dbHelper.insertMenuItem("Pepsi", 1.99, "drink")
        dbHelper.insertMenuItem("Fanta", 2.99, "drink")
    }
}
//si
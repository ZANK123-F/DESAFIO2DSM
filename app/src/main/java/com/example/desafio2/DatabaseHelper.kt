import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "resmexicano.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_MENU = "menu"
        const val TABLE_ORDERS = "orders"
        const val TABLE_HISTORY = "history"
        const val TABLE_USERS = "Users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_TYPE = "type"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla del menú
        val createMenuTable = """
            CREATE TABLE $TABLE_MENU (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_TYPE TEXT
            )
        """.trimIndent()


        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_QUANTITY INTEGER
            )
        """.trimIndent()


        val createHistoryTable = """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_QUANTITY INTEGER,
                order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()


        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()


        db?.execSQL(createMenuTable)
        db?.execSQL(createOrdersTable)
        db?.execSQL(createHistoryTable)
        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MENU")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }


    fun insertMenuItem(name: String, price: Double, type: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_TYPE, type)
        }
        db.insert(TABLE_MENU, null, values)
    }

    fun getMenuItems(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_MENU, null, null, null, null, null, null)
    }


    fun insertOrder(name: String, price: Double, quantity: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_QUANTITY, quantity)
        }
        db.insert(TABLE_ORDERS, null, values)
    }

    fun getOrders(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_ORDERS, null, null, null, null, null, null)
    }

    fun clearOrders() {
        val db = writableDatabase
        db.delete(TABLE_ORDERS, null, null)
    }


    fun insertHistory(name: String, price: Double, quantity: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_QUANTITY, quantity)
        }
        db.insert(TABLE_HISTORY, null, values)
    }

    fun getHistory(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_HISTORY, null, null, null, null, null, null)
    }

    fun addUser(username: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )
        val count = cursor.count
        cursor.close()
        return count > 0
    }
}
//si
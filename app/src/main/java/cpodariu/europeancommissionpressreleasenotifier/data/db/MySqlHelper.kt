package cpodariu.europeancommissionpressreleasenotifier.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import kotlin.math.log

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */
class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PressReleaseDatabase", null, MySqlHelper.DB_VERSION) {

    companion object {
        val DB_VERSION = 3

        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("KeyWord", true,
                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                "word" to TEXT,
                "lastId" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(this.javaClass.simpleName + ": ", "Database upgraded from " + oldVersion.toString() + " to " + newVersion.toString());
        db.dropTable("KeyWord")
        onCreate(db)
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)
package com.example.deathnote

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import com.example.deathnote.HelperClasses.CopyClass
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ActsDbHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
            "${context.packageName}.database_versions",
            Context.MODE_PRIVATE
    )

    private fun installedDatabaseIsOutdated(): Boolean {
        return preferences.getInt(DATABASE_NAME, 0) < DATABASE_VERSION
    }

    private fun writeDatabaseVersionInPreferences() {
        preferences.edit().apply {
            putInt(DATABASE_NAME, DATABASE_VERSION)
            apply()
        }
    }

    private fun installDatabaseFromAssets() {

        val inputStream = FileInputStream(File("data/data/com.example.deathnote/databases/war4.sqlite3"))

        try {
            val outputFile = File(context.getDatabasePath(DATABASE_NAME).path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }
    }

    @Synchronized
    private fun installOrUpdateIfNecessary() {
        if (installedDatabaseIsOutdated()) {
            context.deleteDatabase(DATABASE_NAME)
            installDatabaseFromAssets()
            writeDatabaseVersionInPreferences()
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        throw RuntimeException("The $DATABASE_NAME database is not writable.")
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getReadableDatabase()


    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Nothing to do
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Nothing to do
    }


//    public fun copyToDataBase() {
//        Thread(Runnable {
//            val inputStream = context.assets.open("$DATABASE_NAME.sqlite3")
//            val outFileName = Environment.DIRECTORY_DCIM + File.separator + "hello" + File.separator + "war.sqlite3"
//            val myOutput = FileOutputStream(outFileName);
//
//            val buffer = ByteArray(1024)
//            var length: Int
//            while (inputStream.read(buffer).also { length = it } > 0) {
//                myOutput.write(buffer, 0, length)
//            }
//            myOutput.flush();
//            myOutput.close();
//            inputStream.close();
//        })
//
//    }

    companion object {
        const val DATABASE_NAME = "war1"
        const val DATABASE_VERSION = 5
        const val ASSETS_PATH = "databases"
    }

}

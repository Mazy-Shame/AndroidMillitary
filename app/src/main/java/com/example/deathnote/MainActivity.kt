package com.example.deathnote

import android.app.DownloadManager
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.deathnote.About.About
import com.example.deathnote.AddUser.AddUser
import com.example.deathnote.HelperClasses.CopyClass
import com.example.deathnote.HelperClasses.downloadDB
import com.example.deathnote.Options.Options
import com.example.deathnote.Аuthentication.Authentication
import com.google.android.material.navigation.NavigationView
import java.io.File


class MainActivity : AppCompatActivity() {



    val dbhelp = DataBaseHelper(this)

    val copyhelp = CopyClass(this)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())






        if (!Storage.isAutoraise){

            Thread(Runnable {
                dbhelp.createDataBase()
            }).start()

            startActivity(Intent(applicationContext, Authentication::class.java))
        }
        else {

            upgradedb() //обновление базы данных из кэша
            copyhelp.copy()


            val myDatabase = ActsDbHelper(this).readableDatabase

            setContentView(R.layout.activity_main)

            decompresszip() //распаковка

            setClickonOptions()
            setOnClickOpenPresentation()
            isAdminEnter()
            setOnClickUpdateData()

            val ScrollButton = findViewById<ImageButton>(R.id.swipeButton)
            ScrollButton.setOnClickListener {
                val swipemenu1 = findViewById<DrawerLayout>(R.id.drawer_layout)     //кнопка отрытия меню
                val swipemenu2 = findViewById<NavigationView>(R.id.nav_view)
                swipemenu1.openDrawer(swipemenu2)
            }
//            dbhelp.openDataBase()

//            try {
//                dbhelp.createDataBase()
//            } catch (ioe: IOException) {
//                throw Error("Unable to create database")
//            }
//
//            try {
//                dbhelp.openDataBase()
//            } catch (sqle: SQLException) {
//                throw sqle
//            }





           // val myDatabase = ActsDbHelper(this).readableDatabase!!!!!!!!!!!!!!!!!!!




            val NavArray: ArrayList<String> = ArrayList()
            ConnecttoBase(NavArray, myDatabase)
            addMenuItemNavMenuDrawer(NavArray, myDatabase)



        }
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // создаёт мену опций
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun ConnecttoBase(NavList: ArrayList<String>, myDatabase: SQLiteDatabase) {
        val Str1 = myDatabase.rawQuery("SELECT * FROM navbar", null)
        Str1.moveToFirst()

        while (!Str1.isAfterLast) {
            NavList.add(Str1.getString(Str1.getColumnIndex("NAME")))
            NavList.add(Str1.getString(Str1.getColumnIndex("Id2")))
            Str1.moveToNext()
        }
    }

    private fun addMenuItemNavMenuDrawer(NavList: ArrayList<String> /* список из разделов и их id */, myDatabase: SQLiteDatabase) {
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu

        for (i in 0..NavList.size-1 step 2){
            //SQLLITE вылетает, если вызвать несуществующий эелмент, но можно обработать это с помощтью try catch
            val ButtonsName = myDatabase.rawQuery("SELECT * FROM scrollview WHERE Id2=?", arrayOf(NavList[i + 1])) // получаем таблицу тем в разделе

            menu.add(1, i, 1, NavList[i])
            menu.findItem(i).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
                val background = findViewById<ListView>(R.id.WARWAR)
                background.setBackgroundResource(android.R.color.transparent);
                setListener1(ButtonsName, myDatabase)
            })

            menu.findItem(i).isCheckable = true  // каждый элемент чекабельный
        }
    }

    private fun setListener1(ButtonsName: Cursor, myDatabase: SQLiteDatabase): Boolean {
        ButtonsName.moveToFirst()
        var NavList1: ArrayList<String> = ArrayList()
        var NamesButtons: ArrayList<String> = ArrayList()
        while (!ButtonsName.isAfterLast) {
            NavList1.add(ButtonsName.getString(ButtonsName.getColumnIndex("ButtonName")))
            NavList1.add(ButtonsName.getString(ButtonsName.getColumnIndex("Id3")))
            NamesButtons.add(ButtonsName.getString(ButtonsName.getColumnIndex("ButtonName")))
            ButtonsName.moveToNext()
        }

        val WARWAR = findViewById<ListView>(R.id.WARWAR)
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.itemview, NamesButtons)
        WARWAR.adapter = adapter
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(findViewById<NavigationView>(R.id.nav_view)) // закрытие меню

        // ВЕШАЕМ НА КНОПКИ ЛИНКИ НА ТЕКСТ
        WARWAR.setOnItemClickListener { parent, view, position, id ->
            // вызов функции текста
            // NavList1[(id*2+1).toInt()] - id3 для вызова из третьей таблицы записи

            // получаем текст из БД из таблицы articles
            // val article_text = myDatabase.rawQuery("SELECT * FROM articles WHERE Id3=?", arrayOf(NavList1[(id*2+1).toInt()]))
                // ПРИМЕР ТЕКСТА
                // val article_text = "Текст супер текст\nВторая строка<IMG>test_img.jpg</IMG>Конец"
            // парсер текста в активити с картинками и всем всем, article - указатель на активити или типо того
            // val article = TextToArticle(article_text)
            // тут надо вызвать активити, которое напарсилось
            val article_text = myDatabase.rawQuery("SELECT * FROM article WHERE Id3=?", arrayOf(NavList1[(id * 2 + 1).toInt()]))
            article_text.moveToFirst()
            //val  Text = article_text.getString(article_text.getColumnIndex("NAME")) // беру текст
            //val Img  = article_text.getString(article_text.getColumnIndex("ImageView")) // беру картинку

            fun tryText(): String{      //проверка существует ли text в database, если нет, вернем standart
                try {
                   val Text = article_text.getString(article_text.getColumnIndex("NAME"))
                    return(Text)
                }
                catch (e: NullPointerException) {
                    return("standart")
                }
            }

            fun tryimg(): String{   //проверка существует ли картинка в database, если нет, вернем standart
                try {
                    val Img  = article_text.getString(article_text.getColumnIndex("ImageView"))
                    return(Img)
                }
                catch (e: NullPointerException) {
                    return("standart")
                }
            }

            Storage.img = tryimg()
            Storage.text = tryText()
            startActivity(Intent(applicationContext, Arcticle::class.java))
            Log.d("asdasdasd", Storage.img)
        }

        return true
    }



    private fun upgradedb(){
        val filedatabase = File("data/data/com.example.deathnote/databases/war1")
        val journaldatabase = File("data/data/com.example.deathnote/databases/war1-journal")
        val shared = File("/data/data/com.example.deathnote/shared_prefs/com.example.deathnote.database_versions.xml")
        if (filedatabase.exists()){
            filedatabase.delete()
            journaldatabase.delete()
            shared.delete()
        }
    }


    override fun onBackPressed() {
        //делаем кнопку перехода назад недоступной
    }

    private fun isAdminEnter(){
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu
        if (Storage.isAdmin == true){
            menu.findItem(R.id.adminbutton).isVisible = true
            menu.findItem(R.id.adminbutton).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
                startActivity(Intent(applicationContext, AddUser::class.java))
                true
            })
        }

    }

    private fun setClickonOptions() {                                               //кликлистенер на меню настроек
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu
        menu.findItem(R.id.Settings).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item ->
            startActivity(Intent(applicationContext, Options::class.java))
            true
        })
    }

    private fun setOnClickOpenPresentation(){  ///создатели
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu
        menu.findItem(R.id.about).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item ->
            startActivity(Intent(applicationContext, About::class.java))
            true
        })
    }


    private fun  setOnClickUpdateData(){

        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu
        menu.findItem(R.id.updatedata).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item ->
            val olddb = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/war1.db")
            if (olddb.exists()){
                olddb.delete()
            }
            val down = downloadDB()
            Thread(Runnable {
                val downloadmanager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                down.ondownload("https://war2.ssau.ru/owncloud/index.php/s/pY8ICTgGODq2tjZ/download", downloadmanager)
            }).start()


            true
        })



    }


    private fun decompresszip(){

        val handler = Handler()
        var progressStatus = 0
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

            net.lingala.zip4j.ZipFile(File(path + "/images.zip")).extractAll(path)  //распаковка с помощью библиотеки

    }



}



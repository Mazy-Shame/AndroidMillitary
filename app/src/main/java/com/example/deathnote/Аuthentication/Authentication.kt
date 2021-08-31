package com.example.deathnote.Аuthentication

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.isExternalStorageManager
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.deathnote.HelperClasses.CopyClass
import com.example.deathnote.HelperClasses.downloadDB
import com.example.deathnote.HelperClasses.downloadIMG
import com.example.deathnote.MainActivity
import com.example.deathnote.R
import com.example.deathnote.Storage
import com.example.deathnote.User.User
import com.google.firebase.database.*
import java.io.File

class Authentication : AppCompatActivity() {
    var a: String = ""
    private var MyDatabase: DatabaseReference ?= null
    private var UserKey: String = "Users"
    var ListData: ArrayList<String> = ArrayList()

    val copyhelp = CopyClass(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPermissions()


        setContentView(R.layout.activity_authentication)
        supportActionBar?.hide()
        presentationsdownload()
        init()
        getDatafromDB()
        LoginIn()
        firstimedownloadDB()


    }

    private fun firstimedownloadDB(){
        val olddb = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/war1.db")
        if (!olddb.exists()){
            val down = downloadDB()
            Thread(Runnable {
                val downloadmanager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                down.ondownload(
                    "https://war2.ssau.ru/owncloud/index.php/s/pY8ICTgGODq2tjZ/download",
                    downloadmanager
                )
            }).start()
        }
    }


    override fun onBackPressed() {
        //делаем кнопку перехода назад недоступной
    }


    fun init(){
        MyDatabase = FirebaseDatabase.getInstance().getReference(UserKey)
    }

    fun getDatafromDB(){
        var listen: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(!ListData.isEmpty()){
                    ListData.clear()
                }
                for ( ds: DataSnapshot in p0.children ){
                    val oneuser = ds.getValue(User::class.java)
                    if (oneuser != null) {
                        ListData.add(oneuser.login)
                        ListData.add(oneuser.password)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        MyDatabase?.addValueEventListener(listen)

    }

    private fun LoginIn() {     //Онклик на логинизацию и авторизация
        val mail = findViewById<EditText>(R.id.mailinput)
        val password = findViewById<EditText>(R.id.passwordinput)
        val mailtext = mail.text
        val passwordtext = password.text

        val button = findViewById<Button>(R.id.loginbutton)
        button.setOnClickListener {

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
            val presentationst = File("$path/images.zip")


            if (ListData.size == 0 || presentationst.length().toString() != "184255441") {

                var a = 184255441/100
                var b = presentationst.length().toInt() / a

                Toast.makeText(
                    applicationContext,
                    "Скачано ${b} %",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (ListData.size != 0 && presentationst.length().toString() == "184255441" ) {

                var i : Int = 0
                var isLogin : Boolean = false

                for (i in ListData.indices) {
                    if (ListData[i] == mailtext.toString() && ListData[i + 1] == passwordtext.toString()){
                       // Toast.makeText(applicationContext, "Добро пожаловать", Toast.LENGTH_SHORT).show()
                        isLogin = true
                        Storage.isAutoraise = true                                      //работает но сомнительно
                        if (ListData[i] == "admin" && ListData[i + 1] == "123"){
                            Storage.isAdmin = true
                        }
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                }
                if (isLogin == false){
                    Toast.makeText(
                        applicationContext,
                        "Неправильный логин или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    private fun setPermissions(){

        if (Build.VERSION.SDK_INT <= 29){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
            } else {
//            write()
            }
        }
        else{
            if (isExternalStorageManager()) {
                //todo when permission is granted
            } else {
                //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                Toast.makeText(applicationContext,"Сдвиньте ползунок выше ",Toast.LENGTH_LONG).show()
                startActivity(intent)



            }

        }

       }

    private fun presentationsdownload(){
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        val presentationst = File("$path/images.zip")

        if (!presentationst.exists()){
            val down = downloadIMG()
            Thread(Runnable {
                val downloadmanager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                down.ondownload(
                        "https://war2.ssau.ru/owncloud/index.php/s/a2mEeO4SzOSWUkK/download",
                        downloadmanager
                )
            }).start()
        }

    }



}
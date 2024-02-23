package com.mongodb.tasktracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import org.bson.AbstractBsonWriter

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AlertDialog


class LoginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this)

        //khởi tạo các view
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)

        //thiết lập sự kiện cho nút đăng nhập
        loginButton.setOnClickListener { login() }
    }

    //Khai báo hàm kiểm tra kết nối internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun login() {
        val userUsername = username.text.toString()
        val userPassword = password.text.toString()

        // Kiểm tra kết nối internet
        if (!isNetworkAvailable()) {
            showNoInternetConnectionDialog()
            return
        }

        if (userUsername.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_LONG).show()
            return
        }

        // Sử dụng taskApp từ Application class để đăng nhập
        taskApp.loginAsync(Credentials.emailPassword(userUsername, userPassword)) { result ->
            if (result.isSuccess) {
                Log.v("AUTH", "Successfully logged in.")
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e("AUTH", "Failed to log in: ${result.error}")
                Toast.makeText(this, "Login failed: ${result.error.errorMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showNoInternetConnectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Try Again") { dialog, which ->
                login() // Thử đăng nhập lại
            }
            .setNegativeButton("Exit") { dialog, which ->
                finishAffinity() // Thoát khỏi ứng dụng
            }
            .show()
    }
}

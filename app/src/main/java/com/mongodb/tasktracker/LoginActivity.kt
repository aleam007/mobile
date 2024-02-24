package com.mongodb.tasktracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import org.bson.Document


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var app: App
    private lateinit var rememberMeCheckBox: CheckBox

    private val prefsName = "Final_Project"
    private val usernameKey = "username"
    private val passwordKey = "password"
    private val rememberMeKey = "rememberMe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Khởi tạo Realm
        Realm.init(this)
        app = App(AppConfiguration.Builder("finalproject-rujev").build())

        // Ánh xạ view
        usernameEditText = findViewById(R.id.input_username)
        passwordEditText = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        checkSavedCredentials()


        // Thiết lập sự kiện click cho nút đăng nhập
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            performLogin(username, password)
        }
    }
    //Check app đã được đăng nhập trước đó và nhớ Acc chưa
    private fun checkSavedCredentials() {
        val settings = getSharedPreferences(prefsName, MODE_PRIVATE)
        val savedUsername = settings.getString(usernameKey, "")
        val savedPassword = settings.getString(passwordKey, "")
        val rememberMe = settings.getBoolean(rememberMeKey, false)

        if (rememberMe && !savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameEditText.setText(savedUsername)
            passwordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }
    }

    private fun performLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password must not be empty", Toast.LENGTH_LONG).show()
            return
        }

        // Tạo payload cho Custom Function
        val loginPayload = Document("username", username).append("password", password)

        // Tạo credentials sử dụng Custom Function
        val credentials = Credentials.customFunction(loginPayload)

        // Thực hiện đăng nhập
        app.loginAsync(credentials) { result ->
            if (result.isSuccess) {

                Log.v("AUTH", "Successfully logged in.")
                rememberAccount(username, password, rememberMeCheckBox.isChecked)
                // Lấy User ID
                val userId = app.currentUser()?.id

                // Chuyển đến HomeActivity với User ID
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("USER_ID", userId)
                }

                startActivity(intent)

                // Kết thúc LoginActivity
                finish()
                // Chuyển đến màn hình chính hoặc thực hiện hành động tiếp theo
                // Ví dụ: startActivity(Intent(this, MainActivity::class.java))
            } else {
                Log.e("AUTH", "Failed to log in: ${result.error}")
                Toast.makeText(this, "Login failed: ${result.error.errorMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun rememberAccount(username: String, password: String, rememberMe: Boolean) {
        val settings = getSharedPreferences(prefsName, MODE_PRIVATE)
        settings.edit().apply {
            if (rememberMe) {
                putString(usernameKey, username)
                putString(passwordKey, password)
            } else {
                remove(usernameKey)
                remove(passwordKey)
            }
            putBoolean(rememberMeKey, rememberMe)
            apply()
        }
    }

}

package com.mongodb.tasktracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import com.mongodb.tasktracker.model.User
import io.realm.*
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Khởi tạo Realm
        val realm = Realm.getDefaultInstance()

        // Truy vấn dữ liệu từ bảng User
        val user = realm.where(User::class.java).findFirst()

        // Kiểm tra xem có dữ liệu không
        if (user != null) {
            // In ra thông tin người dùng
            println("Username: ${user.username}")
            println("Password: ${user.password}")
            println("Role: ${user.role}")

            // Kiểm tra và in ra thông tin chi tiết nếu có
            val details = user.details
            if (details != null) {
                println("Name: ${details.name}")
                println("Email: ${details.email}")
            }
        } else {
            println("Không tìm thấy người dùng!")
        }

        // Đóng Realm khi không cần thiết nữa
        realm.close()
    }
}

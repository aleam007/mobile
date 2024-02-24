package com.mongodb.tasktracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.mongodb.tasktracker.databinding.ActivityHomeBinding
import io.realm.Realm
import com.mongodb.tasktracker.model.User
import io.realm.*
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nhận User ID
        val userId = intent.getStringExtra("USER_ID")
        Log.v("USER_ID", "User ID: $userId")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(InterfaceFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.userInterface -> replaceFragment(InterfaceFragment())
                R.id.user -> replaceFragment(InforFragment())
                R.id.gear -> replaceFragment(GearFragment())
                R.id.shop ->replaceFragment(ShopFragment())

                else -> {

                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}

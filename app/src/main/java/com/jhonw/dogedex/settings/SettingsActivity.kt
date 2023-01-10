package com.jhonw.dogedex.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.jhonw.dogedex.R
import com.jhonw.dogedex.auth.LoginActivity
import com.jhonw.dogedex.databinding.ActivitySettingsBinding
import com.jhonw.dogedex.model.User

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        User.logout(this)
        intent = Intent(this, LoginActivity::class.java)
        //todas las tareas que esten debajo de settings seran borradas y se comienza una nueva tarea
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
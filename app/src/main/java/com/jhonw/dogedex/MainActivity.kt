package com.jhonw.dogedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhonw.dogedex.api.ApiServiceInterceptor
import com.jhonw.dogedex.auth.LoginActivity
import com.jhonw.dogedex.databinding.ActivityMainBinding
import com.jhonw.dogedex.doglist.DogListActivity
import com.jhonw.dogedex.model.User
import com.jhonw.dogedex.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)

        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }
    }

    private fun openDogListActivity() {
        intent = Intent(this, DogListActivity::class.java)
        startActivity(intent)
    }

    private fun openSettingsActivity() {
        intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
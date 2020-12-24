package ru.anfilek.navhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemActivity : AppCompatActivity() {

    private val userLogin: UserLogin by lazy { UserLogin(this) }
    // я не совсем понял откуда брать аргументы, поэтому вот
    private var arguments: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        renderItemId()

        findViewById<Button>(R.id.startAgainButton).setOnClickListener {
            startMeAgain()
        }

        findViewById<Button>(R.id.logout).setOnClickListener {
            logout()
        }
    }

    private fun renderItemId() {
        arguments = (1..1000).random()
        findViewById<TextView>(R.id.tvItemId).text = "$arguments"
    }

    private fun startMeAgain() {
        // переходим на ту же активность и удаляем предыдущую из бэкстэка
        val intent = Intent(this, ItemActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun logout() {
        userLogin.setUserLoggedOut()
        // разлогиниваемся и удаляем из бэкстэка активность
        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        // go to login screen
        // pay attention to backstack
    }
}
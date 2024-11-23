package com.example.medcenter

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.view.View as View1

private lateinit var reg: FirebaseAuth

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        reg = FirebaseAuth.getInstance()
    }

    fun auth(view: View1) {
        startActivity(Intent(applicationContext, Authorization::class.java))
    }

    fun reg(email:String, password: String) {
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Введите корректный адрес электронной почты", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show()
            return
        }

        // Регистрация пользователя
        reg.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешная регистрация
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = reg.currentUser
                    updateUI(user)
                } else {
                    // Ошибка регистрации
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Не удалось зарегистрироваться.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    // Вспомогательный метод для проверки валидности email
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = reg.currentUser
        if (currentUser != null) {
            reload()
        }
    }
    private fun reload() {
        TODO("Not yet implemented")
    }
}
package com.corp_2SE.Pick_Asso.data.ui.login.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import com.corp_2SE.Pick_Asso.R
import android.app.ProgressDialog
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.provider.MediaStore
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.corp_2SE.Pick_Asso.RegisterUserInfo
import com.corp_2SE.Pick_Asso.Register_info
import com.corp_2SE.Pick_Asso.data.ui.login.afterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.IOException

class RegisterUser : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        auth = FirebaseAuth.getInstance();

        val promo = resources.getStringArray(R.array.num_promo)
        val username = findViewById<EditText>(R.id.text_mail)
        val password = findViewById<EditText>(R.id.password)


        val sw1 = findViewById<Switch>(R.id.switch_mode)

        sw1.setOnCheckedChangeListener { _ , isChecked ->

            val intent = Intent(this, Register::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@RegisterUser, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@RegisterUser, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                //updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }


            login.setOnClickListener {
                signUpUser()
            }


        }
    }

    private fun signUpUser() {
        val username = findViewById<EditText>(R.id.text_mail)
        val password = findViewById<EditText>(R.id.password)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val nameasso = findViewById<EditText>(R.id.text_name)

        if (username.text.toString().isEmpty()) {
            username.error = "Please enter email"
            username.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            username.error = "Please enter valid email"
            username.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        loading.visibility = View.VISIBLE
        loginViewModel.login(username.text.toString(), password.text.toString())

        auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("inscription", "createUserWithEmail:success")
                    val user = auth.currentUser

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Mail verification", "Email sent.")
                                val intent =Intent(this, RegisterUserInfo::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("inscription", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Inscription failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //updateUiWithUser(currentUser)
        }
    }

    private fun updateUiWithUser(model: FirebaseUser?) {
        val welcome = getString(R.string.welcome)
        val displayName = model?.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */

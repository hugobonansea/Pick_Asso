package com.corp_2SE.Pick_Asso.data.ui.login.ui.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.corp_2SE.Pick_Asso.R
import com.corp_2SE.Pick_Asso.Register_info
import com.corp_2SE.Pick_Asso.data.ui.login.afterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.IOException


class Register : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null

    private lateinit var auth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        //init Firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        auth = FirebaseAuth.getInstance();


        val username = findViewById<EditText>(R.id.text_mail)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val sw1 = findViewById<Switch>(R.id.switch_mode)

        sw1.setOnCheckedChangeListener { _ , isChecked ->
            val intent =Intent(this, RegisterUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        val add_photo = findViewById<Button>(R.id.button_photo)

        add_photo.setOnClickListener {
            addphoto()
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@Register, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            if (filePath!=null){
                login.isEnabled = loginState.isDataValid
            }

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@Register, Observer {
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

            filePath.apply {
                afterTextChanged {
                    if(filePath!=null)
                    {
                        login.isEnabled
                    }
                }
            }
            login.setOnClickListener {
                signUpUser()
            }


        }
    }

    private fun addphoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadPhoto(user: FirebaseUser?) {
        //val user=auth.currentUser
        Log.d("upload", "Entrée")
        if (filePath != null) {
            Log.d("upload", "if")
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/profil/" + (user!!.uid))
            imageRef.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                        val nameasso = findViewById<EditText>(R.id.text_name)

                        val intent =Intent(this, Register_info::class.java).apply {
                            putExtra("Name_asso", nameasso.text.toString())
                        }
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapShot ->
                        val progress = 100.0 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                        progressDialog.setMessage("Uploaded" + progress.toInt() + "%/")
                    }
        }
        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            filePath = data.data;
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
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
                                    uploadPhoto(user)
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
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
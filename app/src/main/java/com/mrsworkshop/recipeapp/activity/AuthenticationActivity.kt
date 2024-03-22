package com.mrsworkshop.recipeapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mrsworkshop.recipeapp.R
import com.mrsworkshop.recipeapp.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : BaseActivity() {
    private lateinit var binding : ActivityAuthenticationBinding
    private lateinit var mAuth : FirebaseAuth

    private var isSignUp : Boolean = false

    private var isSignUpValid : Boolean = false
    private var signUpEmail : String? = null
    private var signUpPassword : String? = null

    private var isLoginValid : Boolean = false
    private var loginEmail : String? = null
    private var loginPassword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        setStatusBarColor(R.color.light_yellow_ffe275)
        checkButtonValidation(false)

        setupComponentListener()
    }

    private fun setupComponentListener() {
        binding.etEditTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.tilOutlinedEmailTextField.error = getString(R.string.auth_activity_sign_up_email_error_text)
                    checkButtonValidation(false)
                }
                else {
                    binding.tilOutlinedEmailTextField.error = null
                    checkButtonValidation(true)
                }

                if (isSignUp) {
                    signUpEmail = s.toString()
                }
                else {
                    loginEmail = s.toString()
                }
            }
        })

        binding.etEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val specialCharacterRegex = "[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]"
                val letterRegex = "[a-zA-Z]"
                val numberRegex = "\\d"

                if (isSignUp) {
                    if (!s.contains(Regex(specialCharacterRegex)) || !s.contains(Regex(letterRegex)) || !s.contains(Regex(numberRegex)) || s.length < 8) {
                        binding.tilOutlinedPasswordTextField.error = getString(R.string.auth_activity_sign_up_password_error_text)
                        checkButtonValidation(false)
                    }
                    else {
                        binding.tilOutlinedPasswordTextField.error = null
                        checkButtonValidation(true)
                    }
                    signUpPassword = s.toString()
                }
                else {
                    binding.tilOutlinedPasswordTextField.error = null
                    loginPassword = s.toString()
                    checkButtonValidation(true)
                }
            }
        })

        binding.txtLoginSignUp.setOnClickListener {
            isSignUp = !isSignUp
            if (!isSignUp) {
                binding.txtAuthenticationTitle.text = getString(R.string.auth_activity_login_welcome_back_text)
                binding.txtAuthenticationDescription.text = getString(R.string.auth_activity_login_login_welcome_text)
                binding.btnAuthenticationLoginSignUp.text = getString(R.string.auth_activity_login_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_do_not_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_signup_text)
                binding.etEditTextEmail.setText("")
                binding.etEditTextPassword.setText("")
                binding.tilOutlinedEmailTextField.error = null
                binding.tilOutlinedPasswordTextField.error = null
            }
            else {
                binding.txtAuthenticationTitle.text = getString(R.string.auth_activity_signup_text)
                binding.txtAuthenticationDescription.text = getString(R.string.auth_activity_login_kindly_register_text)
                binding.btnAuthenticationLoginSignUp.text = getString(R.string.auth_activity_signup_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_already_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_login_text)
                binding.etEditTextEmail.setText("")
                binding.etEditTextPassword.setText("")
                binding.tilOutlinedEmailTextField.error = null
                binding.tilOutlinedPasswordTextField.error = null
            }
        }

        binding.btnAuthenticationLoginSignUp.setOnClickListener {
            if (isSignUp) {
                registerUser(signUpEmail ?: "", signUpPassword ?: "")
            }
            else {
                loginUser(loginEmail ?: "", loginPassword ?: "")
            }
        }
    }

    private fun checkButtonValidation(isValid: Boolean) {
        if (isSignUp) {
            if (!isValid || signUpEmail.isNullOrEmpty() || signUpPassword.isNullOrEmpty()) {
                binding.btnAuthenticationLoginSignUp.alpha = 0.5f
                binding.btnAuthenticationLoginSignUp.isEnabled = false
                isSignUpValid = false
            }
            else {
                binding.btnAuthenticationLoginSignUp.alpha = 1f
                binding.btnAuthenticationLoginSignUp.isEnabled = true
                isSignUpValid = true
            }
        }
        else {
            if (!isValid || loginEmail.isNullOrEmpty() || loginPassword.isNullOrEmpty()) {
                binding.btnAuthenticationLoginSignUp.alpha = 0.5f
                binding.btnAuthenticationLoginSignUp.isEnabled = false
                isLoginValid = false
            }
            else {
                binding.btnAuthenticationLoginSignUp.alpha = 1f
                binding.btnAuthenticationLoginSignUp.isEnabled = true
                isLoginValid = true
            }
        }
    }

    private fun registerUser(email : String, password : String) {
        showLoadingViewDialog()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@AuthenticationActivity, getString(R.string.auth_activity_sign_up_successful_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()

                    val intent = Intent(this@AuthenticationActivity, RecipeListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@AuthenticationActivity, getString(R.string.auth_activity_sign_up_failed_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()
                }
            }
    }

    private fun loginUser(email : String, password : String) {
        showLoadingViewDialog()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@AuthenticationActivity, getString(R.string.auth_activity_login_successful_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()

                    val intent = Intent(this@AuthenticationActivity, RecipeListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@AuthenticationActivity, getString(R.string.auth_activity_login_failed_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()
                }
            }
    }
}
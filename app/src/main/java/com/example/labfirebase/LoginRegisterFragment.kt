package com.example.labfirebase

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.navigation.fragment.findNavController
import com.example.labfirebase.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginRegisterFragment : Fragment() {
    // Validity check flags
    private var passwordConfirmValid: Boolean = false
    private var passwordValid: Boolean = false
    private var usernameValid: Boolean = false

    // Flag indicating the state of the form. True means that the form is used for Login,
    // false means that the form is used to register user.
    private var isLogin = true

    // Firebase Authentication variable
    private lateinit var auth: FirebaseAuth
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set isLogin to true
        isLogin = true

        // Initialize the Firebase authentication variable
        auth = Firebase.auth

        // The LoginRegisterFragment handles both the login and register process
        // The Login form is the default one so the password confirm field is hidden by default
        binding.loginPasswordConfirm.visibility = View.GONE

        // The Login/Register confirm button is disabled until valid data is provided in the form
        binding.loginRegisterButton.isEnabled = false

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validate(): Boolean{
        // Check the state of the fields flags. Depending on the isLogin flag we check either 2
        // or 3 flags (the passwordConfirmValid flag is not used in the Login form)
        return if(isLogin)
            passwordValid && usernameValid
        else
            passwordValid && usernameValid && passwordConfirmValid
    }

    private fun checkUserName(username: String){
        // Check the username data
        Log.i("checkUserName", username)
        if(!isUserNameValid(username)){
            // If the username fails the validity check set an error message in the loginUsername
            // TextInputField. This will change the end icon of the View and also display the
            // message in the Helper text region of the view
            binding.loginUsername.error = getString(R.string.invalid_username)
            // Set the usernameValid flag to false
            usernameValid = false
        } else{
            // If the username passes the test clear the error message and set the flag to true
            binding.loginUsername.error = ""
            usernameValid = true
        }
    }

    private fun checkPassword(password: String){
        // Check the password data
        Log.i("checkPassword", password)
        if(!isPasswordValid(password)){
            // If the password fails the validity check set an error message in the loginPassword
            // TextInputField. This will change the end icon of the View and also display the
            // message in the Helper text region of the view
            binding.loginPassword.error = getString(R.string.invalid_password)
            // Set the passwordValid flag to false
            passwordValid = false
        } else{
            // If the password passes the test clear the error message and set the flag to true
        }
    }

    //////////////////////////////////////////////////
    // VALIDITY CHECKING METHODS
    //
    // Validate
    private fun validate(): Boolean{
        // Check the state of the fields flags. Depending on the isLogin flag we check either 2
        // or 3 flags (the passwordConfirmValid flag is not used in the Login form)
        return if(isLogin)
            passwordValid && usernameValid
        else
            passwordValid && usernameValid && passwordConfirmValid
    }
    //
    // Check user name
    private fun checkUserName(username: String){
        // Check the username data
        Log.i("checkUserName", username)
        if(!isUserNameValid(username)){
            // If the username fails the validity check, set an error message in the loginUsername
            // TextInputField. This will change the end icon of the View and also display the
            // message in the Helper text region of the view.
            binding.loginUsername.error = getString(R.string.invalid_username)
            // Set the usernameValid flag to false
            usernameValid = false
        } else{
            //If the username passes the test clear the error message and set the flag to true
            binding.loginUsername.error = ""
            usernameValid = true
        }
    }
    //
    private fun isUserNameValid(username: String): Boolean{
        return if(username.contains("@")){
            // A valid username contains an "@" symbol and matches an email pattern
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else false
    }
    //
    // Check password
    private fun checkPassword(password: String){
        // Check the password data
        Log.i("checkPassword", password)
        if(!isPasswordValid(password)){
            // If the password fails the validity check, set an error message in the loginPassword
            // TextInputField. This will change the end icon of the View and also display the
            // message in the Helper text region of the view.
            binding.loginPassword.error = getString(R.string.invalid_password)
            // Set the passwordValid flag to false
            passwordValid = false
        } else{
            // If the password passes the test, clear the error message and set the flag to true
            binding.loginPassword.error = ""
            passwordValid = true
        }
    }
    //
    private fun isPasswordValid(password: String): Any {
        // A valid password has at least 6 characters
        return password.length >= 6
    }
    //
    // Check password confirmation
    private fun checkRegisterPassword(passwordConfirm: String, password: String){
        // Check the password confirm data
        Log.i("checkRegisterPassword", "$passwordConfirm $password")
        if(!isPasswordValid(passwordConfirm, password)){
            // If the password confirm fails the validity check, set an error message in the
            // loginPasswordConfirm TextInputField. This will change the end icon of the View and
            // also display the message in the Helper text region of the view
            binding.loginPasswordConfirm.error = getString(R.string.password_mismatch)
            // Set the passwordConfirmValid flag to false
            passwordConfirmValid = false
        } else {
            // If the password confirm passes the test, clear the error message and set the flag to true
            binding.loginPasswordConfirm.error = ""
            passwordConfirmValid = true
        }
    }
    //
    private fun isPasswordConfirmValid(passwordConfirm: String, password: String): Boolean{
        // Password and password confirm have to be the same
        return passwordConfirm == password
    }

    //////////////////////////////////////////////////
    // LOGIN & REGISTER FUNCTIONALITIES
    //
    // login
    private fun login(email: String, password: String){
        // Login process:
        // To sign in the app we use a signInWithEmailAndPassword method from the FirebaseAuth
        // variable and an OnCompleteListener to handle the result.
        // NOTE: This method of authentication has to be enabled in the project
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                // Check the status of the sign in task
                if(task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    updateUI(auth.currentUser)
                } else{
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Snackbar.make(
                        binding.root, "Authentication failed.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }
    //
    // register
    private fun register(email: String, password: String){
        // Login process:
        // To sign up in the app we use a createUserWithEmailAndPassword method from the FirebaseAuth
        // variable and an OnCompleteListener to handle the result.
        // NOTE: This method of authentication has to be enabled in the project
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                // Check the status of the sign up task
                if(task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "createUserWithEmail:success")
                    updateUI(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "createUserWithEmail:failure", task.exception)
                    Snackbar.make(
                        binding.root, "Registering failed.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }
    //
    // update UI
    private fun updateUI(user: FirebaseUser?){
        user ?: return
        // If the user variable isn't null display the message and navigate to the next destination
        // which is the SensorFragment
        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_LoginFragment_to_SensorFragment)
    }
}
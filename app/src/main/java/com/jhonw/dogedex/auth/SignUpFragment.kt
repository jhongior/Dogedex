package com.jhonw.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhonw.dogedex.R
import com.jhonw.dogedex.databinding.FragmentLoginBinding
import com.jhonw.dogedex.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    interface SignUpFragmentActions {
        fun onSignUpFieldsValidated(email: String, password: String, passwordConfirmation: String)
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SignUpFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SignUpFragmet")
        }
    }

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)
        setupSignUpButton()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""

        val email = binding.emailEdit.text.toString()

        if (!isValidEmail(email)) {
            binding.emailInput.error = getString(R.string.email_no_valido)
            return
        }

        val password = binding.passwordEdit.text.toString()

        if (password.isEmpty()) {
            binding.passwordInput.error = getString(R.string.password_no_valido)
            return
        }

        val passwordConfir = binding.confirmPasswordEdit.text.toString()

        if (passwordConfir.isEmpty()) {
            binding.confirmPasswordInput.error = getString(R.string.password_incorrecto)
            return
        }

        if (password != passwordConfir) {
            binding.confirmPasswordInput.error = getString(R.string.password_no_coinciden)
            return
        }

        signUpFragmentActions.onSignUpFieldsValidated(email, password, passwordConfir)
    }

    private fun isValidEmail(email: String?): Boolean {
        return !email.isNullOrEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
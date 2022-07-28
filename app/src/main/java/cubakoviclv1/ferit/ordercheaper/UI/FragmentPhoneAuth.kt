package cubakoviclv1.ferit.ordercheaper.UI

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import cubakoviclv1.ferit.ordercheaper.R
import cubakoviclv1.ferit.ordercheaper.databinding.ContactFragmentBinding
import cubakoviclv1.ferit.ordercheaper.databinding.PhoneAuthFragmentBinding
import java.util.concurrent.TimeUnit

class FragmentPhoneAuth: Fragment() {
    private lateinit var binding: PhoneAuthFragmentBinding
    private var forcedResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = PhoneAuthFragmentBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Code will be sent to your phone number")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                Log.d(TAG, "onCodeSent: $verificationId")
                mVerificationId = verificationId
                forcedResendingToken = token
                progressDialog.dismiss()

                Toast.makeText(requireContext(), "Verification Code Sent...", Toast.LENGTH_SHORT).show()

            }

        }

        binding.btnContinue.setOnClickListener {
            val phone = binding.etPhoneNumber.text.toString()

            if(TextUtils.isEmpty(phone)) {
                Toast.makeText(requireContext(), "Please Enter your Phone Number...", Toast.LENGTH_SHORT).show()
            } else {
                startPhoneNumberVerification(phone)
            }
        }

        binding.tvResendCode.setOnClickListener {
            val phone = binding.etPhoneNumber.text.toString()
            if(TextUtils.isEmpty(phone)) {
                Toast.makeText(requireContext(), "Please Enter your Phone Number...", Toast.LENGTH_SHORT).show()
            } else {
                resendVerificationCode(phone, forcedResendingToken)
            }
        }

        binding.btnVerify.setOnClickListener {
            val code = binding.etVerificationCode.text.toString()
            if(TextUtils.isEmpty(code)) {
                Toast.makeText(requireContext(), "Please Enter Verification Code...", Toast.LENGTH_SHORT).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            val loginFragment = FragmentLogin()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, FragmentLogin())
            transaction.commit()
        }

        return binding.root
    }

    private fun startPhoneNumberVerification(phone: String) {
        progressDialog.setMessage("Verifying Phone Number...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallbacks!!)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(phone: String, token: PhoneAuthProvider.ForceResendingToken?) {
        progressDialog.setMessage("Resending Code...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallbacks!!)
                .setForceResendingToken(token!!)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code:String) {
        progressDialog.setMessage("Verifying Code...")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        progressDialog.setMessage("Logged In")

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    val phone = firebaseAuth.currentUser?.phoneNumber
                    Toast.makeText(requireContext(), "Logged In With $phone", Toast.LENGTH_SHORT).show()

                    val accountSettingsFragment = FragmentAccountSettings()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, FragmentAccountSettings())
                    transaction.commit()

                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                }
    }

}
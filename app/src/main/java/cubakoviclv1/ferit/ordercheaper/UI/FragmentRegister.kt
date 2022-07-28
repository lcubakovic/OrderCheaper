package cubakoviclv1.ferit.ordercheaper.UI

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import cubakoviclv1.ferit.ordercheaper.R
import cubakoviclv1.ferit.ordercheaper.data.UserData
import java.util.*
import javax.crypto.spec.RC2ParameterSpec

class FragmentRegister: Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var fStore: FirebaseFirestore
    lateinit var documentReference: DocumentReference

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.register_fragment, container, false)
        val tv_login = v.findViewById<TextView>(R.id.tv_login)

        val btn_register = v.findViewById<Button>(R.id.btn_register)
        val et_nick = v.findViewById<EditText>(R.id.et_nickname)
        val et_email = v.findViewById<EditText>(R.id.et_email)
        val et_password = v.findViewById<EditText>(R.id.et_password)
        val et_password_confirm = v.findViewById<EditText>(R.id.et_password_confirm)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        fStore = FirebaseFirestore.getInstance()
        databaseReference = database!!.reference.child("user")

        fun register() {
            btn_register.setOnClickListener {
                if (TextUtils.isEmpty(et_nick.text.toString())) {
                    et_nick.setError("Please enter your username!")
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(et_email.text.toString())) {
                    et_email.setError("Please enter youe email!")
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(et_password.text.toString())) {
                    et_password.setError("Please enter your password!")
                    return@setOnClickListener
                }
                else if (TextUtils.isEmpty(et_password.text.toString())) {
                    et_password_confirm.setError("Please confirm your password!")
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(et_password.text.toString())) {
                    et_password_confirm.setError("Password does not match!")
                    return@setOnClickListener
                }


                auth.createUserWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                        .addOnCompleteListener() {
                            if (it.isSuccessful) {
                                val currentUser = auth.currentUser
                                val currentUserDb = databaseReference?.child(currentUser?.uid!!)
                                currentUserDb?.child(("nickname"))?.setValue(et_nick.text.toString())
                                currentUserDb?.child(("email"))?.setValue(et_email.text.toString())
                                currentUserDb?.child(("password"))?.setValue(et_password.text.toString())

                                val setUserData = UserData(et_nick.text.toString(), et_email.text.toString(), et_password.text.toString())

                                documentReference = fStore.collection("users").document(currentUser?.uid!!)
                                val users: MutableMap<String, Any> = HashMap<String, Any>()
                                users.put("Register information", setUserData)
                                documentReference.set(users)

                                Toast.makeText(requireActivity(), "Registration is successful!", Toast.LENGTH_LONG).show()

                                val accountSettingsFragment = FragmentAccountSettings()
                                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.frame_layout, FragmentAccountSettings())
                                transaction.commit()

                            } else if (it.isCanceled) {
                                Toast.makeText(requireActivity(), "Registration is unsuccessful! Try again!!", Toast.LENGTH_LONG).show()
                            }
                        }


            }
        }

        register()

        tv_login.setOnClickListener() {
            val loginFragment = FragmentLogin()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, FragmentLogin())
            transaction.commit()
        }

        return v
    }


}
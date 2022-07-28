package cubakoviclv1.ferit.ordercheaper.UI

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import cubakoviclv1.ferit.ordercheaper.databinding.AccountSettingsFragmentBinding
import cubakoviclv1.ferit.ordercheaper.databinding.FoodFragmentBinding

class FragmentAccountSettings: Fragment() {
    private lateinit var binding: AccountSettingsFragmentBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var fStore: FirebaseFirestore
    lateinit var documentReference: DocumentReference

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = AccountSettingsFragmentBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.reference.child("profile")
        val currentUser = auth.currentUser
        val tv_email = binding.root.findViewById<TextView>(cubakoviclv1.ferit.ordercheaper.R.id.tv_email)
        val tv_userID = binding.root.findViewById<TextView>(cubakoviclv1.ferit.ordercheaper.R.id.tv_userID)
        val tv_phone_number = binding.root.findViewById<TextView>(cubakoviclv1.ferit.ordercheaper.R.id.tv_phone_number)


        fStore = FirebaseFirestore.getInstance()
        documentReference = fStore.collection("users").document(currentUser?.uid!!)


        val user = auth.currentUser

        tv_userID.text = Editable.Factory.getInstance().newEditable("UserID:  " + user?.uid)
        tv_email.text = Editable.Factory.getInstance().newEditable("Email:  " + user?.email)
        tv_phone_number.text = Editable.Factory.getInstance().newEditable("Phone Number: " + user?.phoneNumber)


        return binding.root

    }


}


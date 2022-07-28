package cubakoviclv1.ferit.ordercheaper.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cubakoviclv1.ferit.ordercheaper.databinding.ContactFragmentBinding
import cubakoviclv1.ferit.ordercheaper.databinding.FoodFragmentBinding

class FragmentContact: Fragment() {
    private lateinit var binding: ContactFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}
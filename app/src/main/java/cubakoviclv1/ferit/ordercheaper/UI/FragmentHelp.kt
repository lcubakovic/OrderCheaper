package cubakoviclv1.ferit.ordercheaper.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.rpc.Help
import cubakoviclv1.ferit.ordercheaper.databinding.FoodFragmentBinding
import cubakoviclv1.ferit.ordercheaper.databinding.HelpFragmentBinding

class FragmentHelp: Fragment() {
    private lateinit var binding: HelpFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }
}
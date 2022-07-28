package cubakoviclv1.ferit.ordercheaper.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cubakoviclv1.ferit.ordercheaper.R
import cubakoviclv1.ferit.ordercheaper.databinding.FoodFragmentBinding
import cubakoviclv1.ferit.ordercheaper.databinding.HelpFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create

class FragmentFood: Fragment() {
    private lateinit var binding: FoodFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FoodFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }
}
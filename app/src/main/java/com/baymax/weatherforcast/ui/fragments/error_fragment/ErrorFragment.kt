package com.baymax.weatherforcast.ui.fragments.error_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModel
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModelFactory
import kotlinx.android.synthetic.main.fragment_error.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext


class ErrorFragment : Fragment(), KodeinAware {

    private val args: ErrorFragmentArgs by navArgs()
    private lateinit var main_activity: MainActivity
    override val kodeinContext = kcontext<Fragment>(this)
    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    private lateinit var viewModel: HomeFramentViewModel
    override val kodein by kodein()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onStart() {
        main_activity = (requireActivity() as MainActivity)
        super.onStart()
        if (args.errorMsg.isNotEmpty()) {
            error_text.text = args.errorMsg
        }
        retry_btn.setOnClickListener {
            if (main_activity.isOnline(requireContext())) {
                viewModel = ViewModelProvider(
                    requireActivity(),
                    viewModelFactory
                ).get(HomeFramentViewModel::class.java)
                viewModel.setNetworkAvailable(true)
                findNavController().navigate(R.id.action_errorFragment_to_splashScreenFragment)
            }
        }
    }
}
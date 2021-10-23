package com.baymax.weatherforecast.ui.fragments.error_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.ui.activities.MainActivity
import com.baymax.weatherforecast.ui.fragments.home_fragment.ui.HomeFragmentViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_error.*
import javax.inject.Inject


class ErrorFragment : DaggerFragment() {

    private val args: ErrorFragmentArgs by navArgs()
    private lateinit var main_activity: MainActivity

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeFragmentViewModel
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

        }
    }
}
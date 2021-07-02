package com.baymax.weatherforcast.ui.fragments.error_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_error.*


class ErrorFragment : Fragment() {

    private val args : ErrorFragmentArgs by navArgs()
    private lateinit var main_activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onStart() {
        main_activity  = (requireActivity() as MainActivity)
        super.onStart()
        if(args.errorMsg.isNotEmpty()){
            error_text.text = args.errorMsg
        }
        retry_btn.setOnClickListener {
            if(main_activity.isOnline(requireContext())){
                findNavController().navigate(R.id.action_to_homeFragment)
            }
        }
    }
}
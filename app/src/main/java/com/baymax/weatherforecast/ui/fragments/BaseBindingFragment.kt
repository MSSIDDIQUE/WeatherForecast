package com.baymax.weatherforecast.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforecast.ui.listeners.BaseEventListener
import com.baymax.weatherforecast.ui.view_model.BaseViewModel
import dagger.android.support.DaggerFragment
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

open class BaseBindingFragment<VB : ViewDataBinding, VM : BaseViewModel>(
    val bindingFactory: (LayoutInflater) -> VB
) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var listener: BaseEventListener? = null

    val binding: VB by lazy { bindingFactory(layoutInflater) }

    val viewModel: VM by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[getViewModelClass()]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as BaseEventListener
        } catch (e: IllegalStateException) {
            Log.d("TAG", "MainActivity must implement BaseEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<VM>
    }

    fun showProgressBar(
        isVisible: Boolean,
        message: String? = null
    ) = listener?.showProgressBar(isVisible, message)

    fun showSnackBar(
        message: String,
        actionName: String? = null,
        action: (() -> Unit)? = null
    ) = listener?.showSnackBar(message, actionName, action)
}
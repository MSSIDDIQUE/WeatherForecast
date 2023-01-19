package com.baymax.weather.forecast.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.baymax.weather.forecast.presentation.listeners.BaseEventListener
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
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

    fun getViewModelInstanceWithOwner(
        owner: ViewModelStoreOwner
    ): VM = ViewModelProvider(
        owner,
        viewModelFactory
    )[getViewModelClass()]

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

    fun updateProgressBarState(
        viewState: ProgressBarViewState
    ) = listener?.updateProgressBarState(viewState)

    fun showSnackBar(viewState: SnackBarViewState) = listener?.showSnackBar(viewState)
}

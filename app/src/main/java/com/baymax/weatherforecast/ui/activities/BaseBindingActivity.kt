package com.baymax.weatherforecast.ui.activities

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.databinding.ActivityBaseBinding
import com.baymax.weatherforecast.ui.listeners.BaseEventListener
import com.baymax.weatherforecast.ui.view_model.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseBindingActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    val bindingFactory: (LayoutInflater) -> VB
) : DaggerAppCompatActivity(), BaseEventListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val binding: VB by lazy { bindingFactory(layoutInflater) }

    private lateinit var baseBinding: ActivityBaseBinding

    val viewModel: VM by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[getViewModelClass()]
    }

    override fun setContentView(layoutResID: Int) {
        baseBinding = DataBindingUtil.inflate<ActivityBaseBinding?>(
            layoutInflater, R.layout.activity_base, null, false
        ).apply {
            layoutInflater.inflate(layoutResID, activityContainer, true)
            baseLayoutContainer.requestLayout()
            super.setContentView(root)
        }
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<VM>
    }

    override fun showProgressBar(isVisible: Boolean, message: String?) {
        baseBinding.progressBar.apply {
            groupProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
            if (!message.isNullOrEmpty()){ loadingText.text = message }
        }
    }

    override fun showSnackBar(
        message: String,
        actionName: String?,
        action: (() -> Unit)?
    ) = Snackbar.make(
        baseBinding.baseLayoutContainer,
        message,
        Snackbar.LENGTH_LONG
    ).apply {
        if (actionName != null) {
            setAction(
                actionName
            ) { if (action != null) action() }
        }
    }.show()
}
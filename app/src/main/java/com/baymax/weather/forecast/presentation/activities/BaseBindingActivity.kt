package com.baymax.weather.forecast.presentation.activities

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.databinding.ActivityBaseBinding
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseBindingActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    val bindingFactory: (LayoutInflater) -> VB
) : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val binding: VB by lazy { bindingFactory(layoutInflater) }

    private lateinit var baseBinding: ActivityBaseBinding

    fun getViewModelInstanceWithOwner(
        owner: AppCompatActivity
    ): VM = ViewModelProvider(
        owner,
        viewModelFactory
    )[getViewModelClass()].also { viewModel ->
        with(viewModel) {
            owner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    progressBar.collectLatest { state -> updateProgressBarState(state) }
                    snackBar.collectLatest { state -> showSnackBar(state) }
                }
            }
        }
    }

    override fun setContentView(layoutResID: Int) {
        baseBinding = DataBindingUtil.inflate<ActivityBaseBinding?>(
            layoutInflater,
            R.layout.activity_base,
            null,
            false
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

    private fun updateProgressBarState(
        viewState: ProgressBarViewState
    ) = with(baseBinding.progressBar) {
        root.visibility = when (viewState) {
            ProgressBarViewState.Hide -> View.GONE
            is ProgressBarViewState.Show -> {
                loadingText.text = if (!viewState.msg.isNullOrEmpty()) {
                    viewState.msg
                } else {
                    getString(R.string.loading)
                }
                View.VISIBLE
            }
        }
    }

    private fun showSnackBar(
        viewState: SnackBarViewState
    ) = when (viewState) {
        SnackBarViewState.Nothing -> {}
        is SnackBarViewState.Error -> with(viewState) {
            createSnackBar(errorMessage, actionName, action, R.color.error_red).show()
        }

        is SnackBarViewState.Normal -> with(viewState) {
            createSnackBar(message, actionName, action).show()
        }

        is SnackBarViewState.Success -> with(viewState) {
            createSnackBar(message, actionName, action, R.color.success_green).show()
        }

        is SnackBarViewState.Warning -> with(viewState) {
            createSnackBar(warningMessage, actionName, action, R.color.warning_yellow).show()
        }
    }

    private fun createSnackBar(
        message: String,
        actionName: String?,
        action: (() -> Unit)?,
        backgroundColor: Int? = null
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
        backgroundColor?.let { color ->
            view.setBackgroundColor(ContextCompat.getColor(context, color))
        }
    }
}

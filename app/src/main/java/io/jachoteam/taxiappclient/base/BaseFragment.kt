package io.jachoteam.taxiappclient.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private var activity: AppCompatActivity? = null

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected open fun onInitViewModel() {}
    protected open fun onInitUI(firstInit: Boolean) {}
    protected open fun onObserveViewModel() {}
    protected open fun onSetOnClickListeners() {}

    val baseActivity: AppCompatActivity get() = activity!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInitUI(savedInstanceState == null)
        onObserveViewModel()
        onSetOnClickListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    protected inline fun <reified VM : BaseViewModel> getViewModel(): VM =
        ViewModelProvider(baseActivity, viewModelFactory)[VM::class.java]
}
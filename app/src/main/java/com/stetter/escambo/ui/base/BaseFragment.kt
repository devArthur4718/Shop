package com.stetter.escambo.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.ui.core.CoreViewModel

open class BaseFragment : Fragment() {

    lateinit var mainViewModel: CoreViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            mainViewModel = ViewModelProviders.of(this).get(CoreViewModel::class.java)
        } ?: throw  Throwable("Invalid activity")
    }


}
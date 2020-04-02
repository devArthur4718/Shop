package com.stetter.escambo.ui.core.explore

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.stetter.escambo.R
import com.stetter.escambo.databinding.ExploreFragmentBinding

class ExploreFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel
    private lateinit var binding : ExploreFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.explore_fragment,
            container,
            false)

        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

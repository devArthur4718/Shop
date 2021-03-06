package com.stetter.escambo.ui.core.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ChatFragmentBinding
import com.stetter.escambo.net.models.ProductInterest
import com.stetter.escambo.ui.adapter.RecenteMessageAdapter

class Chat : Fragment() {

    companion object {
        fun newInstance() = Chat()
    }

    private val recentMessageAdapter by lazy { RecenteMessageAdapter() }
    private lateinit var binding: ChatFragmentBinding

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        viewModel.fetchMyInterest()
        setAdapters()
        setObservables()
    }

    private fun setObservables() {
        viewModel.querryByInterest.observe(viewLifecycleOwner, Observer {onProductListReceived(it)   })
    }

    private fun onProductListReceived(dataList: List<ProductInterest>) {
        if (dataList.isEmpty()) {
            // no itens
        } else {
            recentMessageAdapter.data = dataList
        }
    }

    private fun setAdapters() {
        binding.rvMessageList.adapter = recentMessageAdapter
    }

}

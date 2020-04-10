package com.stetter.escambo.ui.core.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.stetter.escambo.R
import com.stetter.escambo.databinding.ChatFragmentBinding
import com.stetter.escambo.net.models.ProductMock
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
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        setAdapters()
        setObservables()
    }

    private fun setObservables() {
        viewModel.listProductMock.observe(viewLifecycleOwner, Observer { onProductListReceived(it) })
    }

    private fun onProductListReceived(productMockList: List<ProductMock>) {
        if (productMockList.isEmpty()) {
            // no itens
        } else {
            recentMessageAdapter.data = productMockList
        }
    }

    private fun setAdapters() {
        binding.rvMessageList.adapter = recentMessageAdapter
    }

}

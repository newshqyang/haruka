package com.sw.haruka.view.explorer

import androidx.recyclerview.widget.LinearLayoutManager
import com.sw.haruka.R
import com.sw.haruka.dal.HarukaConfig
import com.sw.haruka.databinding.ExplorerActBinding
import com.sw.haruka.helper.adapter.SingleTypeAdapter
import com.sw.haruka.view.base.BaseActivity
import com.sw.haruka.view.explorer.viewmodel.ExplorerViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ExplorerActivity : BaseActivity<ExplorerActBinding>() {

    private val mViewModel by viewModel<ExplorerViewModel>()

    private val mAdapter by lazy {
        SingleTypeAdapter(mContext, R.layout.explorer_item, mViewModel.files)
    }

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
            presenter = this@ExplorerActivity
            fileRv.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = mAdapter
            }
        }
    }

    override fun loadData(isRefresh: Boolean) {
        mViewModel.getFiles(HarukaConfig.ROOT_PATH)
    }







    override fun layoutId() = R.layout.explorer_act

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
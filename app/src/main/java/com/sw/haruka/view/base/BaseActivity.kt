package com.sw.haruka.view.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sw.haruka.BR
import com.sw.haruka.helper.utils.AppUtils

/**
 * Created by Swsbty on 2021/03/14
 */
abstract class BaseActivity<VB: ViewDataBinding> : AppCompatActivity(), Presenter {

    protected val mContext by lazy {
        this
    }

    protected val mBinding by lazy {
        DataBindingUtil.setContentView<VB>(mContext, layoutId())
    }

    abstract fun initView()

    abstract fun layoutId(): Int

    abstract fun loadData(isRefresh: Boolean)

    open fun initEvent() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        AppUtils.whiteStatus(mBinding.root)

        initView()
        initEvent()
        loadData(true)
    }

}
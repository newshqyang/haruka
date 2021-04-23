package com.sw.haruka.view.welcome

import android.content.Intent
import com.sw.haruka.R
import com.sw.haruka.databinding.WelcomeActBinding
import com.sw.haruka.helper.extens.navigate2
import com.sw.haruka.helper.utils.AppUtils
import com.sw.haruka.helper.utils.ImageUtil
import com.sw.haruka.helper.utils.LogMgr
import com.sw.haruka.helper.utils.PermissionUtils
import com.sw.haruka.view.main.MainActivity
import com.sw.haruka.view.base.BaseActivity
import com.sw.haruka.view.explorer.ExplorerActivity
import com.sw.haruka.view.welcome.viewmodel.WelcomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseActivity<WelcomeActBinding>() {

    private val mViewModel by viewModel<WelcomeViewModel>()

    override fun initView() {
        permission()
        mBinding.apply {
            vm = mViewModel
        }
        AppUtils.enterFullScreen(mBinding.iv)
    }

    override fun loadData(isRefresh: Boolean) {
        ImageUtil.load(mViewModel.url, mBinding.iv)

    }

    /* 权限检查！ */
    private fun permission() {
        if (PermissionUtils.file(this)) {
            navigate2(MainActivity::class.java)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionUtils.REQUEST_PERMISSION_CODE) {
            permission()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun layoutId() = R.layout.welcome_act

    override fun onPause() {
        AppUtils.exitFullScreen(mBinding.iv)
        super.onPause()
    }
}
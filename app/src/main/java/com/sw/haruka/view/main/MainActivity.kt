package com.sw.haruka.view.main

import android.widget.RelativeLayout
import com.shqyang.yexplorer.ExplorerFragment
import com.sw.haruka.R
import com.sw.haruka.dal.HarukaConfig
import com.sw.haruka.databinding.MainActBinding
import com.sw.haruka.helper.utils.AndroidFileUtils
import com.sw.haruka.helper.utils.PxUtils
import com.sw.haruka.helper.utils.ToastUtils
import com.sw.haruka.ui.widget.EditDialog
import com.sw.haruka.view.base.BaseActivity
import java.io.File

/**
 *  Created by shqyang on 04/24/2021
 */
class MainActivity : BaseActivity<MainActBinding>() {

    private var mExplorerFragment: ExplorerFragment? = null
    private var mEditDialog: EditDialog? = null

    override fun initView() {
        mExplorerFragment = ExplorerFragment(HarukaConfig.ROOT_PATH)
        mEditDialog = EditDialog(mContext)
        mEditDialog?.setSelectAllClickListener {
            mExplorerFragment?.checkAll()
            mEditDialog?.setSelectSizeText(mExplorerFragment!!.checkedSize)
        }
        mEditDialog?.setCancelClickListener {
            mExplorerFragment?.cancelCheckMode()
            mEditDialog?.dismiss()
            moveDownContainer()
        }
        mExplorerFragment?.setClickAction(object : ExplorerFragment.FileClickAction {
            override fun openFile(file: File?) {
                // 打开文件
                AndroidFileUtils.openFile(mContext, file)
            }

            override fun enterCheckMode() {
                mEditDialog?.show()
                moveUpContainer()
            }

            override fun checkChange(checkSize: Int) {
                mEditDialog?.setSelectSizeText(checkSize)
            }

        })
        supportFragmentManager.beginTransaction()
            .add(R.id.container_rl, mExplorerFragment!!)
            .commit()
    }

    /**
     * move container up to show edit dialog
     */
    private fun moveUpContainer() {
        val lp = mBinding.containerRl.layoutParams as RelativeLayout.LayoutParams
        lp.bottomMargin = PxUtils.dip2px(mContext, 80f)
        mBinding.containerRl.layoutParams = lp
    }

    /**
     * move container down to dismiss edit dialog
     */
    private fun moveDownContainer() {
        val lp = mBinding.containerRl.layoutParams as RelativeLayout.LayoutParams
        lp.bottomMargin = PxUtils.dip2px(mContext, 0f)
        mBinding.containerRl.layoutParams = lp
    }

    override fun layoutId() = R.layout.main_act

    override fun loadData(isRefresh: Boolean) {
    }

    override fun onBackPressed() {
        if (mEditDialog!!.isShowing) {
            mEditDialog?.dismiss()
            mExplorerFragment?.cancelCheckMode()
            moveDownContainer()
        } else if (!mExplorerFragment!!.back2previous()) {
            super.onBackPressed()
        }
    }
}
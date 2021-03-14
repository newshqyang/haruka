package com.sw.haruka

import com.sw.haruka.view.explorer.viewmodel.ExplorerViewModel
import com.sw.haruka.view.welcome.viewmodel.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Swsbty on 2021/03/14
 */

val appModule = module {
    viewModel { WelcomeViewModel() }
    viewModel { ExplorerViewModel() }
}
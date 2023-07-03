package com.jerboa.ui.components.register

import android.util.Log
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jerboa.db.AccountViewModel
import com.jerboa.ui.components.home.SiteViewModel
import com.jerboa.ui.components.login.LoginViewModel


@Composable
fun RegisterActivity(
    navController: NavController,
    accountViewModel: AccountViewModel,
    siteViewModel: SiteViewModel,
) {
    Log.d("jerboa", "Got to register activity")

    val snackbarHostState = remember { SnackbarHostState() }
    val ctx = LocalContext.current

    val loginViewModel: LoginViewModel = viewModel()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RegisterHeader(
                navController = navController,
            )
        },
        content = { padding ->
            RegisterForm(
                loading = loginViewModel.loading,
                modifier = Modifier
                    .padding(padding)
                    .imePadding(),
                onClickRegister = { form, instance ->
                },
            )
        },
    )
}

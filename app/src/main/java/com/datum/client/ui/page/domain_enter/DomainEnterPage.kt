package com.datum.client.ui.page.domain_enter

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.Page
import com.datum.client.ui.page.login.LoginPage
import com.datum.client.ui.page.login.LoginPageNavHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DomainEnterPage(navController: NavController, stackEntry: NavBackStackEntry): Page(navController, stackEntry) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {

        val domain = remember {mutableStateOf("")}
        val context = LocalContext.current



        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Spacer(Modifier)
            Text("Enter domain")
            Spacer(Modifier)
            TextField(value = domain.value, onValueChange = { domain.value = it })
            Spacer(Modifier)
            Button(onClick = {
                onButtonClick(domain.value, context)
            }) {
                Text("Check domain")
            }
            Spacer(Modifier)
        }
    }

    private fun onButtonClick(domain: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = BusinessLogicService.instance.checkDomain(domain)
            withContext(Dispatchers.Main){
                val message = if (data) "Server exist" else "Server non exist"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                navController.navigate(LoginPageNavHelper().substituteArgument())
            }
        }
    }
}
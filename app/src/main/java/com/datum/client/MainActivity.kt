package com.datum.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.datum.client.service.BusinessLogicService
import com.datum.client.service.Role
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.theme.DatumTheme
import kotlinx.coroutines.runBlocking
import com.datum.client.ui.page.*
import com.datum.client.ui.page.dataset_control.DatasetControlNavHelper
import com.datum.client.ui.page.dataset_control.DatasetControlPage
import com.datum.client.ui.page.dataset_meta.DatasetMetaNavHelper
import com.datum.client.ui.page.dataset_meta.DatasetMetaPage
import com.datum.client.ui.page.domain_enter.DomainEnterNavHelper
import com.datum.client.ui.page.domain_enter.DomainEnterPage
import com.datum.client.ui.page.login.LoginPage
import com.datum.client.ui.page.login.LoginPageNavHelper
import com.datum.client.ui.page.maintainer.MaintainerPage
import com.datum.client.ui.page.maintainer.MaintainerPageNavHelper
import com.datum.client.ui.page.send_form.SendForm
import com.datum.client.ui.page.send_form.SendFormNavHelper
import com.datum.client.ui.page.user.UserPage
import com.datum.client.ui.page.user_list.UserListNavHelper
import com.datum.client.ui.page.user_list.UserListPage


class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController



    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusinessLogicService.setup(this)
        setContent {
            DatumTheme {
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }


    private fun defineStartPath(): String{
        val service = BusinessLogicService.instance
        return if(service.hasDomain()) {
             runBlocking {
                 if(service.hasRefreshToken()){
                     if(service.restoreSession()){
                         val role = service.getUserRole()
                         if(Role.isUser(role)) UserPage.PATH else MaintainerPageNavHelper().substituteArgument()
                     } else {
                         LoginPageNavHelper().substituteArgument()
                     }
                 } else {
                     LoginPageNavHelper().substituteArgument()
                 }
            }
        } else {
            DomainEnterNavHelper().substituteArgument()
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun App(){
        val startPath = defineStartPath()
        navController = rememberNavController()
        ProgressIndicator.Build()
        NavHost(navController = navController, startDestination = startPath){
            composable(DomainEnterNavHelper().substituteArgument()){
                DomainEnterPage(navController, it).BuildContent()
            }
            composable(LoginPageNavHelper().templateUrl()){
                LoginPage(navController, it).BuildContent()
            }
            composable(UserPage.PATH){
                UserPage(navController, it).BuildContent()
            }
            val maintainerPageNavHelper = MaintainerPageNavHelper()
            composable(maintainerPageNavHelper.templateUrl()){
                MaintainerPage(navController, it).BuildContent()
            }

            val sendForm = SendFormNavHelper()
            composable(sendForm.templateUrl(), arguments = sendForm.getNavArguments()) {
                SendForm(navController, it).BuildContent()
            }

            val userList = UserListNavHelper()
            composable(userList.templateUrl(), arguments = userList.getNavArguments()){
                UserListPage(navController, it).BuildContent()
            }

            val datasetControl  = DatasetControlNavHelper()
            composable(datasetControl.templateUrl(), arguments = datasetControl.getNavArguments()){
                DatasetControlPage(navController, it).BuildContent()
            }

            val datasetMeta  = DatasetMetaNavHelper()
            composable(datasetMeta.templateUrl(), arguments = datasetMeta.getNavArguments()){
                DatasetMetaPage(navController, it).BuildContent()
            }
        }
    }
}
/**
        test user:
            Login: 1
            Password: o<@K4Lxs03@
 **/
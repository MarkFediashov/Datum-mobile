package com.datum.client.ui.page.send_form

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.datum.client.dto.DatasetImageClass
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ComplexDropdownMenu
import com.datum.client.ui.custom.ProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class SendForm(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val image = SendFormNavHelper().getPhoto(backStackEntry)
        val options = BusinessLogicService.instance.getImageClasses()
        val selected = remember{ mutableStateOf(options.first()) }
        val context = LocalContext.current

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Image(bitmap = image.asImageBitmap(), modifier = Modifier.weight(3.5f).padding(30.dp), contentDescription = "", contentScale = ContentScale.FillWidth)
            Box(Modifier.weight(1f)){
                ComplexDropdownMenu(selected, options)
            }
            Button(onClick = { sendData(image, selected.value, context) }) {
                Text("Send")
            }
        }

    }

    private fun sendData(image: Bitmap, imageClass: DatasetImageClass, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            ProgressIndicator.blockOperation {
                val stream = ByteArrayOutputStream(image.byteCount)
                image.compress(Bitmap.CompressFormat.JPEG, 20, stream)
                val result =
                    BusinessLogicService.instance.uploadImage(imageClass.id, stream.toByteArray())
                print(result)
                val message = if (result) "Success" else "Bad"
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }
}
package com.datum.client.ui.page.user

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.material.ExperimentalMaterialApi
import com.datum.client.ui.page.send_form.SendFormNavHelper
import java.nio.ByteBuffer




class UserPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    private fun getBitmapFromImage(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)

        val matrix = Matrix()
        matrix.postRotate(90f)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private val onImageCapture = object : OnImageCapturedCallback() {
        @ExperimentalGetImage
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)
            print(image.height)
            print(image.width)
            val arg = getBitmapFromImage(image)
            val id = ArgumentRepository.putArgument(arg)
            image.close()
            val url = SendFormNavHelper().substituteArgument(id)
            navController.navigate(url)
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
            println(exception.message)
        }
    }

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        CameraPreview()
    }

    @Composable
    fun CameraPreview() {
        val context = LocalContext.current
        val cameraPermission = remember {mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA))}
        val launcher = rememberLauncherForActivityResult (ActivityResultContracts.RequestPermission()) { }

        LaunchedEffect(true){
            when(cameraPermission.value){
                PackageManager.PERMISSION_GRANTED -> { }
                else  -> {
                    launcher.launch(Manifest.permission.CAMERA)
                }
            }
        }
        CameraCapture(context = context)
    }

    @Composable
    fun CameraCapture(
        modifier: Modifier = Modifier,
        cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
        context: Context
    ) {
        val coroutineScope = rememberCoroutineScope()
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder().setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY).setTargetAspectRatio(AspectRatio.RATIO_16_9).build()
            )
        }
        Box(modifier = modifier
            .fillMaxHeight()
            .background(Color.White)) {
            val lifecycleOwner = LocalLifecycleOwner.current
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                }
            )
            Button(
                modifier= Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        imageCaptureUseCase.takePicture(ContextCompat.getMainExecutor(context),  onImageCapture)
                }}
            ) {
                Text("Take photo")
            }

            LaunchedEffect(previewUseCase) {
                val cameraProvider = ProcessCameraProvider.getInstance(context).get()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
    }

    @Composable
    fun CameraPreview(
        modifier: Modifier = Modifier,
        scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
        onUseCase: (UseCase) -> Unit = { }
    ) {
        AndroidView(
            modifier = modifier.fillMaxHeight(),
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                onUseCase(Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                        it.targetRotation = Surface.ROTATION_0
                    }
                )
                previewView
            }
        )
    }
}
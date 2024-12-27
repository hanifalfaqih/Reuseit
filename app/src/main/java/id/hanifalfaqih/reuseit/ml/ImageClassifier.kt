package id.hanifalfaqih.reuseit.ml

import android.content.Context
import android.graphics.Bitmap
import id.hanifalfaqih.reuseit.helper.toByteBufferGrayscale
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ImageClassifier(context: Context, modelResId: Int, labelsResId: Int) {

    private val interpreter: Interpreter
    private val labels: List<String>

    init {
        val model = loadModelFromResources(context, modelResId)
        labels = loadLabelsFromResources(context, labelsResId)
        interpreter = Interpreter(model)
    }

    fun classify(bitmap: Bitmap): String {
        val inputSize = interpreter.getInputTensor(0).shape()[1]
        val byteBuffer = bitmap.toByteBufferGrayscale(inputSize)
        val output = Array(1) { FloatArray(labels.size) }
        interpreter.run(byteBuffer, output)
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        return labels[maxIndex]
    }

    private fun loadModelFromResources(context: Context, resId: Int): ByteBuffer {
        val inputStream = context.resources.openRawResource(resId)
        val byteArray = inputStream.readBytes()
        return ByteBuffer.allocateDirect(byteArray.size).apply {
            order(ByteOrder.nativeOrder())
            put(byteArray)
        }
    }

    private fun loadLabelsFromResources(context: Context, resId: Int): List<String> {
        return context.resources.openRawResource(resId).bufferedReader().readLines()
    }
}
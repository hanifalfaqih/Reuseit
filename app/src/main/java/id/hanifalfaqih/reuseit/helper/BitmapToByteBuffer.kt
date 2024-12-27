package id.hanifalfaqih.reuseit.helper

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Bitmap.toByteBufferGrayscale(inputSize: Int): ByteBuffer {
    // Resize bitmap ke ukuran input model
    val scaledBitmap = Bitmap.createScaledBitmap(this, inputSize, inputSize, true)

    // Buat ByteBuffer untuk menampung data gambar
    val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize).apply {
        order(ByteOrder.nativeOrder())
    }

    // Buat array untuk menampung pixel gambar
    val intValues = IntArray(inputSize * inputSize)
    scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

    // Konversi ke grayscale dan masukkan ke ByteBuffer
    for (pixelValue in intValues) {
        val r = (pixelValue shr 16 and 0xFF)
        val g = (pixelValue shr 8 and 0xFF)
        val b = (pixelValue and 0xFF)

        // Hitung grayscale menggunakan rata-rata RGB
        val gray = (r + g + b) / 3
        byteBuffer.putFloat((gray - 128) / 128.0f) // Normalisasi ke rentang [-1.0, 1.0]
    }

    return byteBuffer
}

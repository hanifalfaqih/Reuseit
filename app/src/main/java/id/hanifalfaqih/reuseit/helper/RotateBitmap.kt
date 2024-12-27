package id.hanifalfaqih.reuseit.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri

fun rotateBitmapIfRequired(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    val exif = inputStream?.let { ExifInterface(it) }
    val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    val rotation = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    return if (rotation != 0f) {
        val matrix = Matrix().apply { postRotate(rotation) }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}
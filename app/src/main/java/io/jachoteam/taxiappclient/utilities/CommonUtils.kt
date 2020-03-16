package io.jachoteam.taxiappclient.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.jachoteam.taxiappclient.R
import java.util.*


fun Context.makeShortToast(textRes: Int) {
    Toast.makeText(this, textRes, Toast.LENGTH_SHORT).show()
}

fun Context.makeShortToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

class Density(val value: Float)

val Number.inDensity: Density
    get() = Density(toFloat())

fun Density.toPixels(displayMetrics: DisplayMetrics) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        displayMetrics
    )

fun Bitmap.toBitmapDescriptor(
    displayMetrics: DisplayMetrics,
    width: Int,
    height: Int): BitmapDescriptor {

    val mWidth = width.inDensity.toPixels(displayMetrics).toInt()
    val mHeight = height.inDensity.toPixels(displayMetrics).toInt()
    val resized = Bitmap.createScaledBitmap(this, mWidth, mHeight, true)
    return BitmapDescriptorFactory.fromBitmap(resized)
}

fun Context.buildTaxiMarker(latLng: LatLng): MarkerOptions {
    val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_taxi)
    val bitmapDescriptor = icon.toBitmapDescriptor(resources.displayMetrics, 32, 16)

    return MarkerOptions()
        .rotation(Random().nextInt(360).toFloat())
        .flat(true)
        .position(latLng)
        .icon(bitmapDescriptor)
}

fun Boolean?.toVisibility(): Int = if (this == true) View.VISIBLE else View.INVISIBLE
package com.example.a4starter

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.max

class SharedViewModel : ViewModel() {
    val desc: MutableLiveData<String> = MutableLiveData()
    val gesturesArray: ArrayList<Gesture?>? = ArrayList<Gesture?>()

    class FloatPoint(var x: Float, var y: Float)

    init {
        desc.value = "Shared model"
    }

    // Referenced in readme
    private fun getPoints(path: Path): Array<FloatPoint?>? {
        val pointArray = arrayOfNulls<FloatPoint>(128)
        val pm = PathMeasure(path, false)
        val length = pm.length
        var distance = 0f
        val speed = length / 128
        var counter = 0
        val aCoordinates = FloatArray(2)
        while (distance < length && counter < 128) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null)
            pointArray[counter] = FloatPoint(
                aCoordinates[0],
                aCoordinates[1]
            )
            counter++
            distance += speed
        }
        return pointArray
    }

    fun getNormalizedPoints(path: Path): Array<FloatPoint?>? {
        val points = getPoints(path)
        val resamplePath = Path()
        if (points != null) {
            resamplePath.moveTo(points[0]!!.x, points[0]!!.y)
            for (point in points) {
                if (point != null && point != points[0]) {
                    resamplePath.lineTo(point.x, point.y)
                }
            }
            val bounds = RectF()
            resamplePath.computeBounds(bounds, false) // fills rect with bounds
            var delta_x = points[0]!!.x - bounds.centerX()
            var delta_y = points[0]!!.y - bounds.centerY()
            var theta = (atan2(delta_y, delta_x)) * 180 / PI
            println("theta radians: $theta")

            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(-theta.toFloat(), bounds.centerX(), bounds.centerY())
            resamplePath.transform(rotateMatrix)

            resamplePath.computeBounds(bounds, false) // fills rect with bounds
            val translationMatrix = Matrix()
            translationMatrix.setTranslate(-bounds.centerX(), -bounds.centerY())
            resamplePath.transform(translationMatrix)

            resamplePath.computeBounds(bounds, false) // fills rect with bounds
            val ratio = 100 / max(max(-bounds.top, bounds.bottom), max(-bounds.left, bounds.right))

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratio, ratio, 0F, 0F)
            resamplePath.transform(scaleMatrix)

            return getPoints(resamplePath)
        }
        return null
    }

    // ... more methods added here
}
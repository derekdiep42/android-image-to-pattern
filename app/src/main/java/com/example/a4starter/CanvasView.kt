package com.example.a4starter

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet


class CanvasView: View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    val LOGNAME = "CanvasView"

    // drawing
    var path: Path? = null
    var paintbrush = Paint(Color.BLUE)
    var bitmap: Bitmap? = null

    // we save a lot of points because they need to be processed
    // during touch events e.g. ACTION_MOVE
    var x1 = 0f
    var y1 = 0f
    private var p1_id = 0
    private var p1_index = 0

    // capture touch events (down/move/up) to create a path/stroke that we draw later
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var inverted: FloatArray
        when (event.pointerCount) {
            1 -> {
                p1_id = event.getPointerId(0)
                p1_index = event.findPointerIndex(p1_id)

                // mapPoints returns values in-place
                inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
                x1 = inverted[0]
                y1 = inverted[1]
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d(LOGNAME, "Action down")
                        path = Path()
                        path!!.moveTo(x1, y1)
                        invalidate()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.d(LOGNAME, "Action move")
                        path!!.lineTo(x1, y1)
                        invalidate()
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d(LOGNAME, "Action up")
//                        sharedViewModel.onePath = path!!
                    }
                }
            }
            else -> {
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (path != null) {
            canvas.drawPath(path!!, paintbrush)
        }
    }

    // constructor
    init {
        paintbrush.style = Paint.Style.STROKE
        paintbrush.strokeWidth = 12f
    }


}

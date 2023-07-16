package com.example.a4starter

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.*
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.graphics.Bitmap

import android.view.View.MeasureSpec
import android.graphics.drawable.Drawable

class AdditionFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null
    private var drawingView: CanvasView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_addition, container, false)

        drawingView = root.findViewById(R.id.canvasView)
        drawingView!!.minimumWidth = 1080
        drawingView!!.minimumHeight = 980
        drawingView!!.setBackgroundColor(Color.rgb(200, 250, 200))

        val buttonOk: Button = root.findViewById(R.id.button_ok)
        buttonOk.setOnClickListener() { _ ->
            if (drawingView?.path != null) {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.context)
                alertDialog.setTitle("Gesture Name")
                val input = EditText(this.context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                alertDialog.setView(input)
                alertDialog.setPositiveButton("OK") { _, _ ->
                    val name = input.text.toString()
                    if (name == "") {
                        val alertDialog2: AlertDialog.Builder = AlertDialog.Builder(this.context)
                        alertDialog2.setTitle("Empty Gesture Name")
                        alertDialog2.setMessage("Empty Gesture Name: Gesture not added")
                        alertDialog2.setPositiveButton("OK") { _, _ -> }
                        alertDialog2.show()
                    } else {
                        val normalizedPoints = mViewModel!!.getNormalizedPoints(drawingView!!.path!!)
                        drawingView!!.bitmap = getBitmapFromView(drawingView!!)
                        val newGesture = Gesture(name, drawingView!!.bitmap, normalizedPoints)
                        mViewModel!!.gesturesArray?.add(newGesture)
                        drawingView!!.path = null
                        drawingView!!.invalidate()
                    }
                }
                alertDialog.setNegativeButton("CANCEL", null)
                alertDialog.show()
            }
        }
        val buttonClear: Button = root.findViewById(R.id.button_clear)
        buttonClear.setOnClickListener() { _ ->
            drawingView!!.path = null
            drawingView!!.invalidate()
        }

        return root
    }

    // Referenced in readme
    private fun getBitmapFromView(view: View): Bitmap? {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(
            1080, 980,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(0, 0, 1080, 980)
        view.draw(canvas)
        return bitmap
    }
}

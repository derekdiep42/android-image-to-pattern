package com.example.a4starter

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*
import kotlin.math.*


class HomeFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null
    private val matchList: ArrayList<Gesture?>? = ArrayList<Gesture?>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val canvasView: CanvasView = root.findViewById(R.id.home_canvas) as CanvasView

        // Referenced in readme
        val adapter = GestureAdapter(this.context, matchList, mViewModel!!,"Home")
        val listView: ListView = root.findViewById(R.id.home_list) as ListView
        listView.adapter = adapter
        canvasView.setBackgroundColor(Color.rgb(200, 250, 200))

        val buttonOk: Button = root.findViewById(R.id.button_ok)
        buttonOk.setOnClickListener() { _ ->
            if (canvasView.path != null) {
                val normalizedPoints = mViewModel!!.getNormalizedPoints(canvasView.path!!)
                val scoresArray = ArrayList<Double>()
                val scoresMap : HashMap<Double, Any> = HashMap<Double, Any>()
                for (gesture in mViewModel!!.gesturesArray!!) {
                    val gestureNormalizedPoints = gesture?.normalizedPoints
                    var sum = 0.0
                    val numPoints = min(gestureNormalizedPoints!!.size, normalizedPoints!!.size)
                    for (i in 0 until numPoints) {
                        sum += sqrt((normalizedPoints[i]!!.x - gestureNormalizedPoints[i]!!.x).pow(2) + (normalizedPoints[i]!!.y - gestureNormalizedPoints[i]!!.y).pow(2))
                    }
                    val score = sum / numPoints
                    scoresArray.add(score)
                    scoresMap[score] = gesture
                }
                scoresArray.sort()
                adapter.clear()
                if (scoresArray.size < 3) {
                    for (i in 0 until scoresArray.size) {
                        adapter.add(scoresMap[scoresArray[i]] as Gesture)
                    }
                } else {
                    adapter.add(scoresMap[scoresArray[0]] as Gesture)
                    adapter.add(scoresMap[scoresArray[1]] as Gesture)
                    adapter.add(scoresMap[scoresArray[2]] as Gesture)
                }

            }
        }
        val buttonClear: Button = root.findViewById(R.id.button_clear)
        buttonClear.setOnClickListener() { _ ->
            canvasView!!.path = null
            canvasView!!.invalidate()
            adapter.clear()
        }

        return root
    }
}
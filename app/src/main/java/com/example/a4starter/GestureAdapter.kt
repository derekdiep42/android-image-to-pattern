package com.example.a4starter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import java.util.ArrayList

// Referenced in readme
class GestureAdapter(context: Context?, users: ArrayList<Gesture?>?, var mViewModel: SharedViewModel, var location: String) :
    ArrayAdapter<Gesture?>(context!!, 0, users!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var view: View? = convertView
        val user = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gesture_item, parent, false)
        }

        // Lookup view for data population
        val gestureImage = view?.findViewById(R.id.imageView) as ImageView
        val gestureName = view?.findViewById(R.id.textView) as TextView
        val gestureButton = view?.findViewById(R.id.button) as Button

        if (location == "Home") {
            gestureButton.visibility = View.GONE
        } else {
            gestureButton.visibility = View.VISIBLE
            gestureButton.text = "Delete"
            gestureButton.setOnClickListener { _ ->
                mViewModel.gesturesArray?.remove(user)
                this.remove(user)
            }
        }

        // Populate the data into the template view using the data object
        gestureImage.setImageBitmap(user!!.bitmap)
        gestureName.text = user!!.name

        // Return the completed view to render on screen
        return view
    }
}

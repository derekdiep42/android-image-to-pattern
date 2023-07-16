package com.example.a4starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class LibraryFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_library, container, false)

        // Referenced in readme
        val adapter = GestureAdapter(this.context, mViewModel!!.gesturesArray, mViewModel!!,"")
        val listView: ListView = root.findViewById(R.id.gesture_list) as ListView
        listView.adapter = adapter

        //mViewModel.gesturesArray.observe

        return root
    }
}
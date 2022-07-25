package com.zehren.photobucket

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pic_list.*

private const val ARG_UID = "UID"

class PicListFragment: Fragment() {
    lateinit var adapter: PhotoBucketAdapter
    lateinit var myContext: Context
    private var uid: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val recyclerView = inflater.inflate(R.layout.fragment_pic_list, container, false) as RecyclerView

        requireActivity().findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            adapter.showPicInfoDialog()
        }

        adapter = PhotoBucketAdapter(myContext, uid!!)
        recyclerView.layoutManager = LinearLayoutManager(myContext)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.remove(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
        return recyclerView
    }

    fun setUpListener(showAll: Boolean){
        adapter.setUpListener(showAll)
    }

    companion object {
        @JvmStatic
        fun newInstance(uid: String) =
            PicListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                }
            }
    }
}
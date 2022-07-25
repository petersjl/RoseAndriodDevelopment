package edu.rosehulman.historicaldocs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DocListFragment : Fragment() {
    private var listener: OnDocSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val recyclerView = inflater.inflate(R.layout.fragment_doc_list, container, false) as RecyclerView
        val adapter = DocListAdapter(context, listener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return recyclerView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDocSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnDockSelectedListener")
        }
    }

    interface OnDocSelectedListener {
        fun onDocSelected(doc: Doc)
    }
}
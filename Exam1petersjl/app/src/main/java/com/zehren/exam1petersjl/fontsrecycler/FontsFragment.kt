package com.zehren.exam1petersjl.fontsrecycler

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zehren.exam1petersjl.FontWrapper
import com.zehren.exam1petersjl.Options
import com.zehren.exam1petersjl.OptionsFragment
import com.zehren.exam1petersjl.R
import kotlinx.android.synthetic.main.fragment_fonts.*

class FontsFragment(val fonts: List<FontWrapper>): Fragment() {

    lateinit var adapter: FontsAdapter
    lateinit var myContext : Context
    lateinit var options: Options

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        options = (myContext as OptionsFragment.OptionsProvider).getAvailableOptions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fonts, container, false)
    }

    override fun onStart() {
        super.onStart()

        adapter = FontsAdapter(myContext, fonts, options)
        fonts_recycler.layoutManager = LinearLayoutManager(myContext)
        fonts_recycler.setHasFixedSize(true)
        fonts_recycler.adapter = adapter
        fonts_recycler.addItemDecoration(DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL))
    }
}
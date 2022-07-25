package com.zehren.photobucket

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.image_detail.view.*

private const val ARG_PIC = "KEY_PIC"

class PicDetailFragment(): Fragment(), GetPicTask.PicDisplayer{
    private var pic: Pic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pic = it.getParcelable<Pic>(ARG_PIC)
            GetPicTask(this).execute(pic!!.url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.image_detail, container, false)
        view.detail_caption.text = pic?.caption
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(pic: Pic)=
            PicDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PIC, pic)
                }
            }

    }

    override fun showPic(bitmap: Bitmap?) {
        view?.detail_image?.setImageBitmap(bitmap)
    }

}
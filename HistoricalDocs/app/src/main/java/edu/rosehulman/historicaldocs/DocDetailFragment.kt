package edu.rosehulman.historicaldocs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_doc_detail.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DOC = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [DocDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var doc: Doc? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doc = it.getParcelable<Doc>(ARG_DOC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doc_detail, container, false)
        view.fragment_doc_detail_title.text = doc?.title
        view.fragment_doc_detail_body.text = doc?.text
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment DocDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(doc: Doc) =
            DocDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DOC, doc)
                }
            }
    }
}
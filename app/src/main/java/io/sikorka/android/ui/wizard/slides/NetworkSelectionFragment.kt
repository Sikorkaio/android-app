package io.sikorka.android.ui.wizard.slides


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.sikorka.android.R


/**
 * A simple [Fragment] subclass.
 * Use the [NetworkSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NetworkSelectionFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater!!.inflate(R.layout.fragment__network_selection, container, false)
  }

  companion object {

    fun newInstance(): NetworkSelectionFragment {
      val fragment = NetworkSelectionFragment()
      return fragment
    }
  }
}

package io.sikorka.android.ui.wizard.slides

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.sikorka.android.R

/**
 * A simple [Fragment] subclass.
 * Use the [InformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformationFragment : androidx.fragment.app.Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment__information, container, false)
  }

  companion object {
    fun newInstance(): InformationFragment {
      return InformationFragment()
    }
  }
}
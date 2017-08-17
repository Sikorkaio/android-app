package io.sikorka.android.ui.wizard.slides


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.android.R
import io.sikorka.android.helpers.fail


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSetupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSetupFragment : Fragment() {

  @OnClick(R.id.account_setup__create_new)
  internal fun onCreateNewPressed() {

  }

  @OnClick(R.id.account_setup__import_account)
  internal fun onAccountImportPressed() {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val layoutInflater = inflater ?: fail("no inflater?")
    val view = layoutInflater.inflate(R.layout.fragment__account_setup, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  companion object {

    fun newInstance(): AccountSetupFragment {
      val fragment = AccountSetupFragment()
      return fragment
    }
  }

}

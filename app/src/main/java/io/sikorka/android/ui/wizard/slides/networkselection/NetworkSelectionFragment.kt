package io.sikorka.android.ui.wizard.slides.networkselection


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.core.configuration.Network
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.showShortSnack
import kotterknife.bindView
import toothpick.Toothpick
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [NetworkSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NetworkSelectionFragment : Fragment(), NetworkSelectionView {

  private val ropstenSelection: TextView by bindView(R.id.network_selection__ropsten)
  private val mainnetSelection: TextView by bindView(R.id.network_selection__mainnet)
  private val rinkebySelection: TextView by bindView(R.id.network_selection__rinkeby)

  @Inject
  lateinit var presenter: NetworkSelectionPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(requireContext().applicationContext, this)
    scope.installModules(NetworkSelectionModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.updateSelected()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment__network_selection, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    mainnetSelection.setOnClickListener {
      mainnetSelection.showShortSnack(R.string.network_selection__network_not_available)
    }

    rinkebySelection.setOnClickListener {
      rinkebySelection.showShortSnack(R.string.network_selection__network_not_available)
    }

    ropstenSelection.setOnClickListener { presenter.selectNetwork(Network.ROPSTEN) }
  }

  override fun updateNetworkSelection(@Network.Selection network: Int) {
    ropstenSelection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    rinkebySelection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    mainnetSelection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    val selection = when (network) {
      Network.ROPSTEN -> ropstenSelection
      Network.RINKEBY -> rinkebySelection
      Network.MAIN_NET -> mainnetSelection
      else -> null
    }

    val context = this.context ?: fail("null context")
    var drawable: Drawable = AppCompatResources.getDrawable(context, R.drawable.ic_check_black_24dp) ?: return
    drawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.colorAccent))
    selection?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    selection?.compoundDrawablePadding = 8
  }

  companion object {

    fun newInstance(): NetworkSelectionFragment {
      val fragment = NetworkSelectionFragment()
      return fragment
    }
  }
}

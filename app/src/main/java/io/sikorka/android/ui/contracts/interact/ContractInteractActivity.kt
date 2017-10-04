package io.sikorka.android.ui.contracts.interact

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.sikorka.android.R
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class ContractInteractActivity : AppCompatActivity(), ContractInteractView {

  @Inject
  lateinit var presenter: ContractInteractPresenter

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), ContractInteractModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__contract_interact)

  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

}
package io.sikorka.android.ui.contracts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.sikorka.android.R
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class DeployContractActivity : AppCompatActivity(), DeployContractView {

  @Inject lateinit var presenter: DeployContractPresenter

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), DeployContractModule())
    super.onCreate(savedInstanceState)
    Toothpick.inject(this, scope)
    setContentView(R.layout.activity__deploy_contract)
  }
}
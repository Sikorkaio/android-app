package io.sikorka.android.ui

import io.sikorka.android.ui.accounts.AccountAdapterPresenter
import io.sikorka.android.ui.accounts.AccountAdapterPresenterImpl
import io.sikorka.android.ui.accounts.AccountPresenter
import io.sikorka.android.ui.accounts.AccountPresenterImpl
import io.sikorka.android.ui.accounts.accountcreation.AccountCreationDialogPresenter
import io.sikorka.android.ui.accounts.accountcreation.AccountCreationDialogPresenterImpl
import io.sikorka.android.ui.accounts.accountexport.AccountExportPresenter
import io.sikorka.android.ui.accounts.accountexport.AccountExportPresenterImpl
import io.sikorka.android.ui.accounts.accountimport.AccountImportPresenter
import io.sikorka.android.ui.accounts.accountimport.AccountImportPresenterImpl
import io.sikorka.android.ui.contracts.DeployContractPresenter
import io.sikorka.android.ui.contracts.DeployContractPresenterImpl
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorPresenter
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorPresenterImpl
import io.sikorka.android.ui.contracts.interact.ContractInteractPresenter
import io.sikorka.android.ui.contracts.interact.ContractInteractPresenterImpl
import io.sikorka.android.ui.contracts.pending.PendingContractsPresenter
import io.sikorka.android.ui.contracts.pending.PendingContractsPresenterImpl
import io.sikorka.android.ui.detector.bluetooth.FindBtDetectorPresenter
import io.sikorka.android.ui.detector.bluetooth.FindBtDetectorPresenterImpl
import io.sikorka.android.ui.main.MainPresenter
import io.sikorka.android.ui.main.MainPresenterImpl
import io.sikorka.android.ui.settings.peermanager.PeerManagerPresenter
import io.sikorka.android.ui.settings.peermanager.PeerManagerPresenterImpl
import io.sikorka.android.ui.wizard.WizardPresenter
import io.sikorka.android.ui.wizard.WizardPresenterImpl
import io.sikorka.android.ui.wizard.slides.accountsetup.AccountSetupPresenter
import io.sikorka.android.ui.wizard.slides.accountsetup.AccountSetupPresenterImpl
import io.sikorka.android.ui.wizard.slides.networkselection.NetworkSelectionPresenter
import io.sikorka.android.ui.wizard.slides.networkselection.NetworkSelectionPresenterImpl
import org.koin.dsl.module.module

val viewModule = module {
  factory { create<AccountCreationDialogPresenterImpl>() as AccountCreationDialogPresenter }
  factory { create<AccountImportPresenterImpl>() as AccountImportPresenter }
  factory { create<AccountAdapterPresenterImpl>() as AccountAdapterPresenter }
  factory { create<AccountPresenterImpl>() as AccountPresenter }
  factory { create<AccountExportPresenterImpl>() as AccountExportPresenter }
  factory { create<NetworkSelectionPresenterImpl>() as NetworkSelectionPresenter }
  factory { create<AccountSetupPresenterImpl>() as AccountSetupPresenter }
  factory { create<PendingContractsPresenterImpl>() as PendingContractsPresenter }
  factory { create<DeployContractPresenterImpl>() as DeployContractPresenter }
  factory { create<DeployDetectorPresenterImpl>() as DeployDetectorPresenter }

  factory { create<PeerManagerPresenterImpl>() as PeerManagerPresenter }
  factory { create<WizardPresenterImpl>() as WizardPresenter }
  factory { create<MainPresenterImpl>() as MainPresenter }
  factory { create<FindBtDetectorPresenterImpl>() as FindBtDetectorPresenter }
  factory { create<ContractInteractPresenterImpl>() as ContractInteractPresenter }
}
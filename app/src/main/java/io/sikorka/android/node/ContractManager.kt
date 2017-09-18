package io.sikorka.android.node

import io.sikorka.android.node.accounts.AccountRepository
import javax.inject.Inject

class ContractManager
@Inject constructor(
    private val gethNode: GethNode,
    private val accountRepository: AccountRepository
) {

}
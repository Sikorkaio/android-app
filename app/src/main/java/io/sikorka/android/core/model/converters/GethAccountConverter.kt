package io.sikorka.android.core.model.converters

import org.ethereum.geth.Account
import io.sikorka.android.core.model.Account as SikorkaAccount

class GethAccountConverter : Converter<Account, SikorkaAccount> {
  private val addressConverted: GethAddressConverter = GethAddressConverter()
  override fun convert(from: Account): SikorkaAccount {
    return SikorkaAccount(addressConverted.convert(from.address), from.url)
  }
}
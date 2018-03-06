package io.sikorka.android.core.model.converters

import org.ethereum.geth.Address
import org.ethereum.geth.Geth
import io.sikorka.android.core.model.Address as SikorkaAddress

class SikorkaAddressConverter : Converter<SikorkaAddress, Address> {
  override fun convert(from: SikorkaAddress): Address = Geth.newAddressFromHex(from.hex)
}
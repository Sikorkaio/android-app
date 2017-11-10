package io.sikorka.android.eth.converters

import org.ethereum.geth.Address
import org.ethereum.geth.Geth
import io.sikorka.android.eth.Address as SikorkaAddress


class SikorkaAddressConverter : Converter<SikorkaAddress, Address> {
  override fun convert(from: SikorkaAddress): Address = Geth.newAddressFromHex(from.hex)
}
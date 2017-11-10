package io.sikorka.android.eth.converters

import org.ethereum.geth.Address
import io.sikorka.android.eth.Address as SikorkaAddress

class GethAddressConverter : Converter<Address, SikorkaAddress> {
  override fun convert(from: Address): SikorkaAddress = SikorkaAddress(from.hex)
}
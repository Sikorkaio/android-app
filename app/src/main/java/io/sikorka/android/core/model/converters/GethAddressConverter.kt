package io.sikorka.android.core.model.converters

import org.ethereum.geth.Address
import io.sikorka.android.core.model.Address as SikorkaAddress

class GethAddressConverter : Converter<Address, SikorkaAddress> {
  override fun convert(from: Address): SikorkaAddress = SikorkaAddress(from.hex)
}
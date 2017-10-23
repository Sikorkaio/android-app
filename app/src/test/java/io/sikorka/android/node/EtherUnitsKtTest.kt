package io.sikorka.android.node

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EtherUnitsKtTest {
  @Test
  fun findUnitEther() {
    assertThat(findUnit(5 * 1000000000000000000)).isEqualTo(EtherUnits.ETHER)
  }

  @Test
  fun findUnitFinney() {
    assertThat(findUnit(681 * 100000000000000)).isEqualTo(EtherUnits.FINNEY)
  }
  @Test
  fun findUnitSzabo() {
    assertThat(findUnit(507 * 1000000000000)).isEqualTo(EtherUnits.SZABO)
  }

  @Test
  fun findUnitGwei() {
    assertThat(findUnit(402 * 1000000000L)).isEqualTo(EtherUnits.GWEI)
  }

  @Test
  fun findUnitMwei() {
    assertThat(findUnit(301 * 1000000L)).isEqualTo(EtherUnits.MWEI)
  }

  @Test
  fun findUnitKwei() {
    assertThat(findUnit(205 * 1000L)).isEqualTo(EtherUnits.KWEI)
  }

  @Test
  fun findUnitWei() {
    assertThat(findUnit(192)).isEqualTo(EtherUnits.WEI)
  }

  @Test
  fun findUnitZeroWei() {
    assertThat(findUnit(0)).isEqualTo(EtherUnits.WEI)
  }
}
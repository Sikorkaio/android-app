package io.sikorka.android.data.location

data class UserLocation(
    val set: Boolean,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
  companion object {
    fun default(): UserLocation = UserLocation(false)

    fun set(latitude: Double, longitude: Double): UserLocation {
      return UserLocation(true, latitude, longitude)
    }
  }
}

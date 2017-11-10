package io.sikorka.android.eth.converters


interface Converter<in FROM, out TO> {
  fun convert(from: FROM): TO
}
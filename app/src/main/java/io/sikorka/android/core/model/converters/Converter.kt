package io.sikorka.android.core.model.converters


interface Converter<in FROM, out TO> {
  fun convert(from: FROM): TO
}
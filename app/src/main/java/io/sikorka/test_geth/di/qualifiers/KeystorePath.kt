package io.sikorka.test_geth.di.qualifiers

import javax.inject.Qualifier

/** Qualifies binding related to the keystore file path. **/
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KeystorePath
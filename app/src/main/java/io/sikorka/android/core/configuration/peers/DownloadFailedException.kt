package io.sikorka.android.core.configuration.peers

class DownloadFailedException(code: Int) : Exception("download failed due to http code $code")
package io.sikorka.android

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.url(): String {
  return "http://$hostName:$port"
}

fun mockServer(): MockWebServer {
  val server = MockWebServer()
  server.start()
  return server
}

fun mockResponse(response: MockResponse.() -> Unit): MockResponse {
  val mockResponse = MockResponse()
  response(mockResponse)
  return mockResponse
}

fun MockResponse.success() {
  this.setResponseCode(200)
}

fun MockResponse.code(code: Int) {
  setResponseCode(code)
}

fun MockResponse.body(body: String) {
  setBody(body)
}
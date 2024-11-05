package it.spike.image.upload.ImageUpload

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx.vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import it.spike.image.upload.ImageUpload.api.GetImage
import it.spike.image.upload.ImageUpload.api.PostImage
import it.spike.image.upload.ImageUpload.repo.InMemoryFile

fun main(){
  val vertx = vertx()
  vertx.deployVerticle(MainVerticle())
}


class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val router = Router.router(vertx)
    router.route().handler(
      CorsHandler.create("*")
      .allowedMethod(io.vertx.core.http.HttpMethod.GET)
      .allowedMethod(io.vertx.core.http.HttpMethod.POST)
      .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
      .allowedHeader("Access-Control-Request-Method")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Headers")
      .allowedHeader("Content-Type"));
    val files = InMemoryFile()
    PostImage(router, vertx, files)
    GetImage(router, vertx, files)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080).onComplete { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8080")
        } else {
          startPromise.fail(http.cause());
        }
      }

  }
}

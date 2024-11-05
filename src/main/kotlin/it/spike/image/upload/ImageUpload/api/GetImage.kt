package it.spike.image.upload.ImageUpload.api


import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.multipart.MultipartForm
import it.spike.image.upload.ImageUpload.repo.InMemoryFile
import java.util.*

class GetImage(
  router: Router,
  private val vertx: Vertx,
  private val inMemoryFile: InMemoryFile
) : Handler<RoutingContext> {

  init {
    router.get("/api/file")
      .handler(BodyHandler.create())
      .handler(this)
  }

  override fun handle(context: RoutingContext) {
    val fileName = context.queryParam("name").first()

    val image = inMemoryFile.getBy(fileName)

    val fileType = when {
      fileName.endsWith(".pdf") -> "application/pdf"
      fileName.endsWith(".zip") -> "application/zip"
      fileName.endsWith(".txt") -> "text/plain"
      fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") -> "image/jpeg"
      fileName.endsWith(".png") -> "image/png"
      else -> "application/octet-stream"
    }

    /*val b64 = Base64.getEncoder().encode(image.bytes)

    val json = JsonObject.of("image", b64)

    context
      .response()
      .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(json.toString())*/
    context
      .response()
      .putHeader(HttpHeaderNames.CONTENT_TYPE, fileType)
      .putHeader(HttpHeaderNames.CONTENT_LENGTH, image.length().toString())
      //.putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=$fileName")
      .end(image)
  }

}

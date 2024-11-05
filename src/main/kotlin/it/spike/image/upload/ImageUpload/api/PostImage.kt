package it.spike.image.upload.ImageUpload.api

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import it.spike.image.upload.ImageUpload.repo.InMemoryFile
import java.net.URLDecoder

class PostImage(
  router: Router,
  private val vertx: Vertx,
  private val inMemoryFile: InMemoryFile
) : Handler<RoutingContext> {

  init {
    router.post("/api/file")
      .handler(BodyHandler.create().setMergeFormAttributes(true))
      .handler(this)
  }

  override fun handle(context: RoutingContext) {

    val fileUploadSet = context.fileUploads();
    val fileUploadIterator = fileUploadSet.iterator();
    var files = emptyList<String>()
    while (fileUploadIterator.hasNext()) {
      val fileUpload = fileUploadIterator.next();
      val uploadedFile = vertx.fileSystem().readFileBlocking(fileUpload.uploadedFileName());
      val fileName = URLDecoder.decode(fileUpload.fileName(), "UTF-8");
      files = files + listOf(fileName)
      inMemoryFile.add(fileName, uploadedFile)
    }
    context
      .response()
      .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
      .send("File/s saved as $files")
  }
}

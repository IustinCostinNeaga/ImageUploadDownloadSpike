package it.spike.image.upload.ImageUpload.repo

import io.vertx.core.buffer.Buffer

class InMemoryFile {

  private var files: Map<String, Buffer> = emptyMap()

  fun add(name: String, file: Buffer){
    files = files + mapOf(name to file)
  }

  fun getBy(name: String): Buffer{
    return files[name]!!
  }
}

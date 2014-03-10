package com.gilt.thehand.exceptions

case class CannotDeserializeException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

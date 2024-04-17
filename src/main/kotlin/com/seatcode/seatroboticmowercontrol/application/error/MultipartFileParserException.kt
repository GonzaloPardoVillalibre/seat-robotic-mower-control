package com.seatcode.seatroboticmowercontrol.application.error

/**
 * Custom exception to be thrown on MultiPart File parsing errors
 */
class MultipartFileParserException(message: String) : RuntimeException(message)
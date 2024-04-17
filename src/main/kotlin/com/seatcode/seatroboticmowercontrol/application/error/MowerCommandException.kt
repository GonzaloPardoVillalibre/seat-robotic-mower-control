package com.seatcode.seatroboticmowercontrol.application.error

/**
 * Custom exception to be thrown on mover command execution errors
 */
class MowerCommandException(message: String) : RuntimeException(message)
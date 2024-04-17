package com.seatcode.seatroboticmowercontrol.application.error

const val ERROR_WORKFLOW = "Error creating mowing workflow"
const val INVALID_PLATEAU = "Invalid plateau coordinates. Expected two positive integers"
const val INVALID_MOWER = "Invalid mower position or orientation at line"
const val INVALID_MOWER_COMMANDS = "Invalid mower commands at line"
const val MISSING_MOWER_COMMANDS = "Missing commands for mower at line"
const val DUPLICATED_INITIAL_COORDINATES = "Invalid plateau coordinates. One ore two mowers have the same initial position"
const val ILLEGAL_INITIAL_COORDINATES = "Invalid plateau coordinates. One ore two mowers are out of boundaries"
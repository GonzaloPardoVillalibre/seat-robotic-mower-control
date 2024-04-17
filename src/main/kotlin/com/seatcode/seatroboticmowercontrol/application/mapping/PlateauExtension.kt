package com.seatcode.seatroboticmowercontrol.application.mapping

import com.seatcode.seatroboticmowercontrol.domain.Plateau


/**
 * Extension function for Plateau to MowingWorkflow OutputString
 */
fun Plateau.toMowingWorkflowOutputString(): String {
    return this.mowersWithCommands
        .map { it.first.getCoordinates().x.toString() + " " + it.first.getCoordinates().y.toString() + " " + it.first.getOrientation() }
        .toString()
        .replace(", ", "\n")
        .removePrefix("[")
        .removeSuffix("]")
        .trimStart()
}
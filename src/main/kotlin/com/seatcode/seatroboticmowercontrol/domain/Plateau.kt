package com.seatcode.seatroboticmowercontrol.domain


class Plateau(var size: Coordinates, var mowersWithCommands: List<Pair<Mower, String>>) {

    /**
     * Return a list of occupied coordinates by mowers.
     */
    fun occupiedCoordinates(): List<Coordinates> {
        return this.mowersWithCommands.map { it.first.getCoordinates() }
    }
}
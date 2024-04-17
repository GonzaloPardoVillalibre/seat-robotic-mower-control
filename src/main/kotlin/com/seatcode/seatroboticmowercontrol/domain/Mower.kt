package com.seatcode.seatroboticmowercontrol.domain

class Mower(private var coordinates: Coordinates, private var orientation: Orientation) {

    fun move() {
       coordinates = calculateNextPosition()
    }

    fun getCoordinates(): Coordinates {
        return coordinates
    }

    fun getOrientation(): Orientation {
        return orientation
    }

    fun spinLeft() {
        orientation = when (orientation) {
            Orientation.N -> Orientation.W
            Orientation.E -> Orientation.N
            Orientation.S -> Orientation.E
            Orientation.W -> Orientation.S
        }
    }

    fun spinRight() {
        orientation = when (orientation) {
            Orientation.N -> Orientation.E
            Orientation.E -> Orientation.S
            Orientation.S -> Orientation.W
            Orientation.W -> Orientation.N
        }
    }

    fun calculateNextPosition(): Coordinates {
        val nextPosition = Coordinates(coordinates.x, coordinates.y)
        when (orientation) {
            Orientation.N -> nextPosition.y++
            Orientation.E -> nextPosition.x++
            Orientation.S -> nextPosition.y--
            Orientation.W -> nextPosition.x--
        }
        return nextPosition
    }

}
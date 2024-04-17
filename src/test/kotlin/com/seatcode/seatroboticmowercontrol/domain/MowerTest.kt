package com.seatcode.seatroboticmowercontrol.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MowerTest {
    private lateinit var mower: Mower

    @BeforeEach
    fun setup() {
        mower = Mower(Coordinates(0, 0), Orientation.N)
    }

    @Test
    fun `test initial state`() {
        assertEquals(Coordinates(0, 0), mower.getCoordinates())
        assertEquals(Orientation.N, mower.getOrientation())
    }

    @Test
    fun `test move north`() {
        mower.move()
        assertEquals(Coordinates(0, 1), mower.getCoordinates())
    }

    @Test
    fun `test spin left from north to west`() {
        mower.spinLeft()
        assertEquals(Orientation.W, mower.getOrientation())
    }

    @Test
    fun `test spin right from north to east`() {
        mower.spinRight()
        assertEquals(Orientation.E, mower.getOrientation())
    }

    @Test
    fun `test moving in all directions`() {
        mower.move()
        assertEquals(Coordinates(0, 1), mower.getCoordinates())

        mower.spinRight()
        mower.move()
        assertEquals(Coordinates(1, 1), mower.getCoordinates())

        mower.spinRight()
        mower.move()
        assertEquals(Coordinates(1, 0), mower.getCoordinates())

        mower.spinRight()
        mower.move()
        assertEquals(Coordinates(0, 0), mower.getCoordinates())
    }

    @Test
    fun `test full rotation to the right`() {
        mower.spinRight()
        mower.spinRight()
        mower.spinRight()
        mower.spinRight()
        assertEquals(Orientation.N, mower.getOrientation())
    }

    @Test
    fun `test full rotation to the left`() {
        mower.spinLeft()
        mower.spinLeft()
        mower.spinLeft()
        mower.spinLeft()
        assertEquals(Orientation.N, mower.getOrientation())
    }

    @Test
    fun `test movement updates coordinates correctly`() {
        mower = Mower(Coordinates(5, 5), Orientation.S)
        mower.move()
        assertEquals(Coordinates(5, 4), mower.getCoordinates())
    }
}

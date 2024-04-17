package com.seatcode.seatroboticmowercontrol.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class PlateauTest {

    @Test
    fun `occupiedCoordinates should return empty list for empty plateau`() {
        val plateau = Plateau(Coordinates(5, 5), listOf())
        assertEquals(emptyList<Coordinates>(), plateau.occupiedCoordinates())
    }

    @Test
    fun `occupiedCoordinates should return correct coordinates for populated plateau`() {
        val mower1 = mock(Mower::class.java)
        val mower2 = mock(Mower::class.java)
        `when`(mower1.getCoordinates()).thenReturn(Coordinates(1, 2))
        `when`(mower2.getCoordinates()).thenReturn(Coordinates(3, 4))

        val plateau = Plateau(Coordinates(5, 5), listOf(
            Pair(mower1, "command1"),
            Pair(mower2, "command2")
        ))

        assertEquals(listOf(Coordinates(1, 2), Coordinates(3, 4)), plateau.occupiedCoordinates())
    }
}

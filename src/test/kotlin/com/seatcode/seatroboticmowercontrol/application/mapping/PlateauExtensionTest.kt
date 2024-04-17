package com.seatcode.seatroboticmowercontrol.application.mapping

import com.seatcode.seatroboticmowercontrol.domain.Plateau
import com.seatcode.seatroboticmowercontrol.domain.Mower
import com.seatcode.seatroboticmowercontrol.domain.Coordinates
import com.seatcode.seatroboticmowercontrol.domain.Orientation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class PlateauExtensionTest {

    @Test
    fun `toOutputString should format single mower output correctly`() {
        val mower = mock(Mower::class.java)
        val coordinates = Coordinates(1, 2)

        `when`(mower.getCoordinates()).thenReturn(coordinates)
        `when`(mower.getOrientation()).thenReturn(Orientation.N)

        val plateau = Plateau(Coordinates(10, 10), listOf(mower to "LLMR"))

        val expectedOutput = "1 2 N"
        assertEquals(expectedOutput, plateau.toMowingWorkflowOutputString())
    }

    @Test
    fun `toOutputString should format multiple mowers output correctly`() {
        val mower1 = mock(Mower::class.java)
        val mower2 = mock(Mower::class.java)
        val mower3 = mock(Mower::class.java)
        val coordinates1 = Coordinates(1, 2)
        val coordinates2 = Coordinates(3, 4)
        val coordinates3 = Coordinates(8, 8)
        `when`(mower1.getCoordinates()).thenReturn(coordinates1)
        `when`(mower1.getOrientation()).thenReturn(Orientation.N)
        `when`(mower2.getCoordinates()).thenReturn(coordinates2)
        `when`(mower2.getOrientation()).thenReturn(Orientation.E)
        `when`(mower3.getCoordinates()).thenReturn(coordinates3)
        `when`(mower3.getOrientation()).thenReturn(Orientation.S)

        val plateau = Plateau(
            Coordinates(10, 10), listOf(
                mower1 to "LLMR",
                mower2 to "MMLR",
                mower3 to "LLRRMMM"
            )
        )

        val expectedOutput = "1 2 N\n3 4 E\n8 8 S"
        assertEquals(expectedOutput, plateau.toMowingWorkflowOutputString())
    }
}

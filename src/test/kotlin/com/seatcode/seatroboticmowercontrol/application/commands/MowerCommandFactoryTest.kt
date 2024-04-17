package com.seatcode.seatroboticmowercontrol.application.commands

import com.seatcode.seatroboticmowercontrol.application.error.MowerCommandException
import com.seatcode.seatroboticmowercontrol.domain.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class CommandTests {

    @Mock
    private lateinit var mower: Mower

    @Mock
    private lateinit var plateau: Plateau

    @Test
    fun `createCommand returns appropriate Command types`() {
        val factory = MowerCommandFactory()
        assertTrue(factory.createCommand('M', mower, plateau) is MoveCommand)
        assertTrue(factory.createCommand('L', mower, plateau) is SpinLeftCommand)
        assertTrue(factory.createCommand('R', mower, plateau) is SpinRightCommand)
    }

    @Test
    fun `createCommand throws for invalid command`() {
        val factory = MowerCommandFactory()
        assertThrows(MowerCommandException::class.java) {
            factory.createCommand('X', mower, plateau)
        }
    }

    // MoveCommand
    @Test
    fun `MoveCommand moves mower within bounds and not occupied`() {
        val nextPosition = Coordinates(1, 1)
        whenever(mower.calculateNextPosition()).thenReturn(nextPosition)
        whenever(plateau.size).thenReturn(Coordinates(5, 5))
        whenever(plateau.occupiedCoordinates()).thenReturn(listOf(Coordinates(2, 2)))

        val command = MoveCommand(mower, plateau)
        command.execute()

        verify(mower).move()
    }

    @Test
    fun `MoveCommand does not move mower if position is occupied`() {
        val nextPosition = Coordinates(1, 1)
        whenever(mower.calculateNextPosition()).thenReturn(nextPosition)
        whenever(plateau.size).thenReturn(Coordinates(5, 5))
        whenever(plateau.occupiedCoordinates()).thenReturn(listOf(nextPosition))

        val command = MoveCommand(mower, plateau)
        command.execute()

        verify(mower, never()).move()
    }

    @Test
    fun `MoveCommand throws if mower moves out of bounds`() {
        val nextPosition = Coordinates(6, 6)
        whenever(mower.calculateNextPosition()).thenReturn(nextPosition)
        whenever(plateau.size).thenReturn(Coordinates(5, 5))

        val command = MoveCommand(mower, plateau)

        assertThrows(MowerCommandException::class.java) {
            command.execute()
        }
    }

    // Tests for SpinLeftCommand and SpinRightCommand
    @Test
    fun `SpinLeftCommand calls spinLeft on mower`() {
        val command = SpinLeftCommand(mower)
        command.execute()
        verify(mower).spinLeft()
    }

    @Test
    fun `SpinRightCommand calls spinRight on mower`() {
        val command = SpinRightCommand(mower)
        command.execute()
        verify(mower).spinRight()
    }
}

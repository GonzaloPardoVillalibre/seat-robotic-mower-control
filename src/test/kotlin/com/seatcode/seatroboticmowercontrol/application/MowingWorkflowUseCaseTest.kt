package com.seatcode.seatroboticmowercontrol.application

import com.seatcode.seatroboticmowercontrol.application.commands.Command
import com.seatcode.seatroboticmowercontrol.application.commands.CommandFactory
import com.seatcode.seatroboticmowercontrol.domain.Coordinates
import com.seatcode.seatroboticmowercontrol.domain.Mower
import com.seatcode.seatroboticmowercontrol.domain.Plateau
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.web.multipart.MultipartFile

@ExtendWith(MockitoExtension::class)
internal class MowingWorkflowUseCaseTest {

    @Mock
    private lateinit var parser: MultipartFileParser

    @Mock
    private lateinit var commandFactory: CommandFactory

    @Mock
    private lateinit var file: MultipartFile

    @Mock
    private lateinit var plateau: Plateau

    @Mock
    private lateinit var mower: Mower

    @Mock
    private lateinit var command: Command

    @Test
    fun `execute should handle successful workflow`() {
        `when`(parser.parse(file)).thenReturn(plateau)
        `when`(plateau.mowersWithCommands).thenReturn(listOf(Pair(mower, "FLR")))
        `when`(commandFactory.createCommand(any(), any() , any())).thenReturn(command)
        `when`(mower.getCoordinates()).thenReturn(Coordinates(3,3))

        val useCase = MowingWorkflowUseCase(parser, commandFactory)
        useCase.execute(file)

        verify(command, times(3)).execute()
    }

    @Test
    fun `execute should throw exception on parser exceptions`() {
        `when`(parser.parse(file)).thenThrow(RuntimeException("Parsing error"))

        val useCase = MowingWorkflowUseCase(parser, commandFactory)

        val exception = Assertions.assertThrows(RuntimeException::class.java) {
            useCase.execute(file)
        }
        Assertions.assertEquals("Parsing error", exception.message)
    }

    @Test
    fun `execute should handle exception on command Factory failure`() {
        `when`(parser.parse(file)).thenReturn(plateau)
        `when`(plateau.mowersWithCommands).thenReturn(listOf(Pair(mower, "FLR")))
        `when`(commandFactory.createCommand(any(), any(), any())).thenThrow(RuntimeException("Command Factory error"))
        `when`(mower.getCoordinates()).thenReturn(Coordinates(3,3))

        val useCase = MowingWorkflowUseCase(parser, commandFactory)
        useCase.execute(file)
        verify(command, times(0)).execute()
    }

    @Test
    fun `execute should handle exception on command failure and stop execution`() {
        `when`(parser.parse(file)).thenReturn(plateau)
        `when`(plateau.mowersWithCommands).thenReturn(listOf(Pair(mower, "FLR")))
        `when`(command.execute())
            .then { doNothing() }
            .thenThrow(RuntimeException("Command error"))
        `when`(commandFactory.createCommand(any(), any() , any())).thenReturn(command)
        `when`(mower.getCoordinates()).thenReturn(Coordinates(3,3))

        val useCase = MowingWorkflowUseCase(parser, commandFactory)
        useCase.execute(file)

        verify(command, times(1)).execute()
    }
}

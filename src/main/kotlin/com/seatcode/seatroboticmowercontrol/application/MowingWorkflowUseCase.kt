package com.seatcode.seatroboticmowercontrol.application

import com.seatcode.seatroboticmowercontrol.application.commands.CommandFactory
import com.seatcode.seatroboticmowercontrol.application.income.MowingWorkflowPort
import com.seatcode.seatroboticmowercontrol.application.mapping.toMowingWorkflowOutputString
import com.seatcode.seatroboticmowercontrol.domain.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * MowingWorkflow Port implementation.
 */
@Service
class MowingWorkflowUseCase(
    private val parser: MultipartFileParser,
    private val mowerCommandFactory: CommandFactory
) : MowingWorkflowPort {
    private val log = LoggerFactory.getLogger(MowingWorkflowUseCase::class.java)

    override fun execute(file: MultipartFile): String {
        val plateau: Plateau = parser.parse(file)

        plateau.mowersWithCommands.forEachIndexed()
        { mowerIndex, mowerWithCommands ->
            executeMowerCommands(mowerWithCommands.first, mowerWithCommands.second, plateau, mowerIndex)
        }

        return plateau.toMowingWorkflowOutputString()
    }

    private fun executeMowerCommands(mower: Mower, commands: String, plateau: Plateau, mowerIndex: Int) {
        log.info("Initiation execution for Mower ${mowerIndex + 1} at ${mower.getCoordinates()} with command instructions: $commands")
        for (index in commands.indices) {
            try {
                val command = mowerCommandFactory.createCommand(commands[index], mower, plateau)
                command.execute()
            } catch (e: Exception) {
                log.warn(
                    "Error executing command ${index + 1} " +
                            "for mower number ${mowerIndex + 1} at ${mower.getCoordinates()}: ${e.message}"
                )
                break
            }
        }
        log.info("Execution for Mower ${mowerIndex + 1} finished at ${mower.getCoordinates()}")
    }
}
package com.seatcode.seatroboticmowercontrol.application

import com.seatcode.seatroboticmowercontrol.application.error.*
import com.seatcode.seatroboticmowercontrol.domain.Coordinates
import com.seatcode.seatroboticmowercontrol.domain.Mower
import com.seatcode.seatroboticmowercontrol.domain.Orientation
import com.seatcode.seatroboticmowercontrol.domain.Plateau
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Parses and validates the MultiPart File into a Plateau
 */
@Component
class MultipartFileParser {

    fun parse(file: MultipartFile): Plateau {
        val lines = readLines(file)
        val plateauSize = extractSize(lines.first())
        val mowerConfigurations = extractMowerConfigurations(lines)

        return Plateau(plateauSize, mowerConfigurations).also {
            validateInitialPositions(it)
        }
    }

    private fun readLines(file: MultipartFile): List<String> {
        return BufferedReader(InputStreamReader(file.inputStream, StandardCharsets.UTF_8)).use { reader ->
            val lines = reader.readLines()
            if (lines.isEmpty()) throw MultipartFileParserException("Input file is empty")
            lines
        }
    }

    /**
     * Extracts all the Mowers and their associated commands
     */
    private fun extractMowerConfigurations(lines: List<String>): List<Pair<Mower, String>> {
        return (1 until lines.size step 2).map { index ->
            extractMower(index, lines[index]) to extractMowerCommands(index, lines)
        }
    }

    /**
     * Extracts Mower's associated commands and validates them
     */
    private fun extractMowerCommands(i: Int, lines: List<String>): String {
        return (i + 1 < lines.size).takeIf { it }
            ?.let {
                val commandsLine = lines[i + 1].trim()
                validateCommandsLine(commandsLine, i)
                commandsLine
            }
            ?: throw MultipartFileParserException("$MISSING_MOWER_COMMANDS ${i + 2}")
    }


    /**
     * Validates the input and extracts a Mower
     */
    private fun extractMower(i: Int, line: String): Mower {
        val positionLine = line.trim().split(" ")
        validateMowerInitialPosition(positionLine, i)
        return Mower(
            Coordinates(positionLine[0].toInt(), positionLine[1].toInt()),
            Orientation.valueOf(positionLine[2])
        )
    }

    /**
     * Validates the input and extracts the Plateau
     */
    private fun extractSize(firstLine: String): Coordinates {
        val plateau = firstLine.trim().split(" ")
        validatePlateau(plateau)
        return Coordinates(plateau[0].toInt(), plateau[1].toInt())

    }

    /**
     * Validates a single command line
     */
    private fun validateCommandsLine(commandsLine: String, i: Int) {
        val validCommands = setOf('L', 'R', 'M')
        if (commandsLine.any { it !in validCommands }) {
            throw MultipartFileParserException("$INVALID_MOWER_COMMANDS ${i + 2}")
        }
    }

    /**
     * Validates the Plateau line
     */
    private fun validatePlateau(plateau: List<String>) {
        if (plateau.size != 2 || plateau.any { it.toIntOrNull() == null }) {
            throw MultipartFileParserException(INVALID_PLATEAU)
        }
    }

    /**
     * Validates a Mower initial position
     */
    private fun validateMowerInitialPosition(positionLine: List<String>, i: Int) {
        if (positionLine.size != 3 ||
            positionLine[0].toIntOrNull() == null ||
            positionLine[1].toIntOrNull() == null ||
            positionLine[2].length != 1 ||
            !isValidOrientation(positionLine[2])
        ) {
            throw MultipartFileParserException("$INVALID_MOWER ${i + 1}")
        }
    }

    /**
     * Validates a Mower initial orientation
     */
    private fun isValidOrientation(orientation: String): Boolean {
        return try {
            Orientation.valueOf(orientation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /**
     * Validates Mowers are inside boundaries and  have different starting positions
     */
    private fun validateInitialPositions(plateau: Plateau): Boolean {

        plateau.occupiedCoordinates().also { coordinatesList ->
            coordinatesList.find { it.x !in 0 .. plateau.size.x || it.y !in 0 .. plateau.size.y }
                ?.let {
                    throw MultipartFileParserException("$ILLEGAL_INITIAL_COORDINATES $it")
                }
        }

        plateau.occupiedCoordinates()
            .groupBy { it }
            .filter { it.value.size > 1 }
            .keys
            .takeIf { it.isNotEmpty() }
            ?.let {
                throw MultipartFileParserException("$DUPLICATED_INITIAL_COORDINATES $it")
            }

        return true
    }
}
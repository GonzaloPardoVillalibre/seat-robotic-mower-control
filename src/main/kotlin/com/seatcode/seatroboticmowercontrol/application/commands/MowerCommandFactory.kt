package com.seatcode.seatroboticmowercontrol.application.commands

import com.seatcode.seatroboticmowercontrol.application.error.MowerCommandException
import com.seatcode.seatroboticmowercontrol.domain.Mower
import com.seatcode.seatroboticmowercontrol.domain.Plateau
import org.springframework.stereotype.Component

@Component
class MowerCommandFactory : CommandFactory {
    override fun createCommand(commandChar: Char, mower: Mower, plateau: Plateau): Command {
        return when (commandChar) {
            'M' -> MoveCommand(mower, plateau)
            'L' -> SpinLeftCommand(mower)
            'R' -> SpinRightCommand(mower)
            else -> throw MowerCommandException("Invalid command: $commandChar") //Should not happen after validation
        }
    }
}

class MoveCommand(private val mower: Mower, private val plateau: Plateau) : Command {
    override fun execute() {
        val nextPosition = mower.calculateNextPosition()

        // Validate if next position is out of boundaries and throw exception
        if (nextPosition.x !in 0 .. plateau.size.x || nextPosition.y !in 0 .. plateau.size.y)
            throw MowerCommandException("The mower is willing to move out of the Plateau")

        // Validate if the next position is occupied and skip the movement it is
        if (nextPosition !in plateau.occupiedCoordinates())
            mower.move()
    }
}

class SpinLeftCommand(private val mower: Mower) : Command {
    override fun execute() {
        mower.spinLeft()
    }
}

class SpinRightCommand(private val mower: Mower) : Command {
    override fun execute() {
        mower.spinRight()
    }
}
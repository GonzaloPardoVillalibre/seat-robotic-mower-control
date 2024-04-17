package com.seatcode.seatroboticmowercontrol.application.commands

import com.seatcode.seatroboticmowercontrol.domain.Mower
import com.seatcode.seatroboticmowercontrol.domain.Plateau

interface CommandFactory {
    fun createCommand(commandChar: Char, mower: Mower, plateau: Plateau): Command
}
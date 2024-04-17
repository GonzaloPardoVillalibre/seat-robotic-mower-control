package com.seatcode.seatroboticmowercontrol.application.income

import org.springframework.web.multipart.MultipartFile

/**
 * MowingWorkflow Port (Hexagonal ports and adapters).
 */
interface MowingWorkflowPort {
    fun execute(file: MultipartFile) : String
}
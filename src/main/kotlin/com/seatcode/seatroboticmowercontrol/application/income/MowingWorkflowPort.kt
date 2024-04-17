package com.seatcode.seatroboticmowercontrol.application.income

import org.springframework.web.multipart.MultipartFile

interface MowingWorkflowPort {
    fun execute(file: MultipartFile) : String
}
package com.seatcode.seatroboticmowercontrol.adapter

import com.seatcode.seatroboticmowercontrol.application.income.MowingWorkflowPort
import com.seatcode.seatroboticmowercontrol.application.error.ERROR_WORKFLOW
import com.seatcode.seatroboticmowercontrol.application.error.MultipartFileParserException
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * MowingWorkflow Adapter (Hexagonal ports and adapters).
 */
@RestController
@RequestMapping("/api/workflow")
class MowingWorkflowController(private val mowingWorkflowUseCase: MowingWorkflowPort) {
    private val logger = LoggerFactory.getLogger(MowingWorkflowController::class.java)

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE] )
    fun createWorkflowFromFile(@RequestPart(value = "file") workFlowFile: MultipartFile): ResponseEntity<String> {
        return try {
            ResponseEntity.ok(mowingWorkflowUseCase.execute(workFlowFile))
        } catch (ex: MultipartFileParserException) {
            logger.error(ERROR_WORKFLOW, ex)
            ResponseEntity.badRequest().body("$ERROR_WORKFLOW: ${ex.message}")
        } catch (ex: Exception) {
            logger.error(ERROR_WORKFLOW, ex)
            ResponseEntity.internalServerError().body("$ERROR_WORKFLOW: ${ex.message}")
        }
    }
}
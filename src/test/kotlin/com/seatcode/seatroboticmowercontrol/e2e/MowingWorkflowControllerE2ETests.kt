package com.seatcode.seatroboticmowercontrol.e2e

import com.seatcode.seatroboticmowercontrol.infrastructure.main.SeatRoboticMowerControlApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SeatRoboticMowerControlApplication::class]
)
internal class MowingWorkflowControllerE2ETests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun testCreateAssessmentWorkflowFromFile() {

        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("test-assessment-workflow.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.OK, response.statusCode)
        assert(
            response.body?.contains(
                "1 3 N\n" +
                        "5 1 E"
            ) ?: false
        )
    }

    @Test
    fun testCreateLongWorkflowFromFile() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("test-long-workflow.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.OK, response.statusCode)
        assert(
            response.body?.contains(
                "0 2 W\n" +
                        "7 3 E\n" +
                        "7 0 S\n" +
                        "0 10 W\n" +
                        "8 5 N"
            ) ?: false
        )
    }

    @Test
    fun testCreateWorkflowFromFileDuplicatedInitialCoordinates() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/duplicatedInitialCoordinates.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Invalid plateau coordinates. One ore two mowers have the same initial position [Coordinates(x=1, y=2)]")
            ?.let { assert(it) }
    }

    @Test
    fun testCreateWorkflowFromFileIllegalInitialCoordinates() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/illegalInitialCoordinates.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Invalid plateau coordinates. One ore two mowers are out of boundaries Coordinates(x=11, y=2)")
            ?.let { assert(it) }
    }

    @Test
    fun testCreateWorkflowFromFileInvalidMowerCommands() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/invalidMowerCommands.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Invalid mower commands at line 13")
            ?.let { assert(it) }
    }

    @Test
    fun testCreateWorkflowFromFileInvalidOrientation() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/invalidOrientation.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Invalid mower position or orientation at line 2")
            ?.let { assert(it) }
    }

    @Test
    fun testCreateWorkflowFromFileInvalidPlateauSize() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/invalidPlateauSize.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Invalid plateau coordinates. Expected two positive integers")
            ?.let { assert(it) }
    }

    @Test
    fun testCreateWorkflowFromFileMissingMowerCommands() {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", ClassPathResource("/fixtures/missingMowerCommands.txt"))

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(parameters, headers)

        // Perform POST request
        val response = restTemplate.exchange(
            "http://localhost:$port/api/workflow",
            HttpMethod.POST,
            requestEntity,
            String::class.java, ""
        )

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        response.body?.contains("Error creating mowing workflow: Missing commands for mower at line 5")
            ?.let { assert(it) }
    }
}

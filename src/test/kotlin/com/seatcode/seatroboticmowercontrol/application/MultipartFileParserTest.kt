import com.seatcode.seatroboticmowercontrol.application.MultipartFileParser
import com.seatcode.seatroboticmowercontrol.application.error.MultipartFileParserException
import com.seatcode.seatroboticmowercontrol.domain.Coordinates
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.mock.web.MockMultipartFile
import java.nio.charset.StandardCharsets

internal class MultipartFileParserTest {

    @Test
    fun `test valid file input converts to Workflow correctly`() {
        val parser = MultipartFileParser()
        val content = this::class.java.getResource("fixtures/validWorkflow.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "validInput.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val workflow = parser.parse(multipartFile)

        assertNotNull(workflow)
        assertEquals(2, workflow.mowersWithCommands.size)
        assertEquals(Coordinates(10, 10), workflow.size)
    }

    @Test
    fun `test empty file throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val multipartFile = MockMultipartFile("file", "emptyInput.txt", "text/plain", ByteArray(0))
        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertEquals("Input file is empty", exception.message)
    }

    @Test
    fun `test file with invalid plateau format throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content = this::class.java.getResource("fixtures/invalidPlateauSize.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "invalidPlateauSize.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Invalid plateau"))
    }

    @Test
    fun `test file with missing mower commands throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content =
            this::class.java.getResource("fixtures/missingMowerCommands.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "missingMowerCommands.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Missing commands for mower"))
    }

    @Test
    fun `test file with invalid mower commands throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content =
            this::class.java.getResource("fixtures/invalidMowerCommands.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "invalidMowerCommands.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Invalid mower commands"))
    }

    @Test
    fun `test file with invalid orientation throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content = this::class.java.getResource("fixtures/invalidOrientation.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "invalidOrientation.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Invalid mower position"))
    }

    @Test
    fun `test file with illegal original coordinates throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content = this::class.java.getResource("fixtures/illegalInitialCoordinates.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "invalidOrientation.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Invalid plateau coordinates. One ore two mowers are out of boundaries"))
    }

    @Test
    fun `test file with duplicate original coordinates throws MultipartFileParserException`() {
        val parser = MultipartFileParser()
        val content = this::class.java.getResource("fixtures/duplicatedInitialCoordinates.txt")?.readText()
        val multipartFile = MockMultipartFile(
            "file", "invalidOrientation.txt", "text/plain",
            content?.toByteArray(StandardCharsets.UTF_8)
        )

        val exception = assertThrows(MultipartFileParserException::class.java) {
            parser.parse(multipartFile)
        }

        assertTrue(exception.message!!.contains("Invalid plateau coordinates. One ore two mowers have the same initial position"))
    }
}
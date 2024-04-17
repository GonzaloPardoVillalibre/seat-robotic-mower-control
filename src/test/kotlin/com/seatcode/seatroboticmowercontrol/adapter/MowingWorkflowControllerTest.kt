package com.seatcode.seatroboticmowercontrol.adapter

import com.seatcode.seatroboticmowercontrol.application.error.MultipartFileParserException
import com.seatcode.seatroboticmowercontrol.application.income.MowingWorkflowPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
internal class MowingWorkflowControllerTest {

    @Mock
    private lateinit var mowingWorkflowUseCase: MowingWorkflowPort

    @InjectMocks
    private lateinit var mowingWorkflowController: MowingWorkflowController

    private lateinit var mockMvc: MockMvc

    private val testFileContent = "test data".toByteArray()
    private val testFile = MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, testFileContent)

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mowingWorkflowController).build()
    }

    @Test
    fun `createWorkflowFromFile should return OK when use case executes successfully`() {
        `when`(mowingWorkflowUseCase.execute(testFile)).thenReturn("Workflow created successfully")

        mockMvc.perform(multipart("/api/workflow").file(testFile))
            .andExpect(status().isOk())
            .andExpect(content().string("Workflow created successfully"))

        verify(mowingWorkflowUseCase).execute(testFile)
    }

    @Test
    fun `createWorkflowFromFile should return Internal Server Error on FileAlreadyExistsException`() {
        `when`(mowingWorkflowUseCase.execute(testFile)).thenThrow(MultipartFileParserException("Test Exception"))

        mockMvc.perform(multipart("/api/workflow").file(testFile))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error creating mowing workflow: Test Exception"))

        verify(mowingWorkflowUseCase).execute(testFile)
    }

    @Test
    fun `createWorkflowFromFile should return Internal Server Error on exception`() {
        `when`(mowingWorkflowUseCase.execute(testFile)).thenThrow(RuntimeException("Test Exception"))

        mockMvc.perform(multipart("/api/workflow").file(testFile))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Error creating mowing workflow: Test Exception"))

        verify(mowingWorkflowUseCase).execute(testFile)
    }
}

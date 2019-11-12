package community.flock.eco.feature.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import community.flock.eco.feature.project.inputs.ProjectInput
import community.flock.eco.feature.project.model.Project
import community.flock.eco.feature.project.repositories.ProjectRepository
import community.flock.eco.feature.project.services.ProjectService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProjectIT {

    val mapper = ObjectMapper().registerModule(JSR310Module());

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @Test
    fun `get all projects`() {

        createProjects(1..5)

        mockMvc.perform(get("/api/projects")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("$.length()").value(5))
    }


    @Test
    fun `post project`() {

        val input = ProjectInput(name = "Project 1")

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(input))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("$.name").value("Project 1"))
    }


    private fun createProjects(range: IntRange) = range
            .map { Project(code = "project-$it", name = "Project $it") }
            .run { projectRepository.saveAll(this) }
}

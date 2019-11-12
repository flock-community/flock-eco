package community.flock.eco.feature.project.controllers

import community.flock.eco.core.utils.toResponse
import community.flock.eco.feature.project.inputs.ProjectInput
import community.flock.eco.feature.project.services.ProjectService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller("/api/projects")
class ProjectController(
        private val projectService: ProjectService) {


    @GetMapping
    fun list(pageable: Pageable) = projectService
            .findAll(pageable)
            .toResponse()

    @PostMapping
    fun post(@RequestBody input: ProjectInput) = projectService
            .create(input)
            .toResponse()

    @PutMapping("{code}")
    fun put(
            @PathVariable code: String,
            @RequestBody input: ProjectInput) = projectService
            .update(code, input)
            .toResponse()
}

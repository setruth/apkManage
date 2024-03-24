package com.setruth.apkmanage.controller

import com.setruth.apkmanage.model.ProjectInfoEntity
import com.setruth.apkmanage.service.ProjectService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("projects")
@CrossOrigin
@Tag(name = "项目管理")
class ProjectController(
    private val projectService: ProjectService,
) {
    @GetMapping
    @Operation(summary = "获取所有项目信息")
    @ApiResponse(useReturnTypeSchema = true)
    fun getProjects() = projectService.projects()

    @Operation(
        summary = "删除某个项目",
        parameters = [
            Parameter(name = "projectId", description = "项目Id", required = true, example = ""),
        ]
    )
    @DeleteMapping("{projectId}")
    @ApiResponse(useReturnTypeSchema = true)
    fun delProject(@PathVariable("projectId") projectId: String) = projectService.del(projectId)
    @Operation(
        summary = "新增项目信息",
        parameters = [
            Parameter(name = "projectName", description = "项目名称", required = true, example = ""),
        ]
    )
    @PostMapping("{projectName}")
    @ApiResponse(useReturnTypeSchema = true)
    fun addProject(@PathVariable("projectName") projectName: String) = projectService.add(projectName)

    @Operation(
        summary = "更新项目",
        parameters = [
            Parameter(name = "projectId", description = "项目Id", required = true, example = ""),
            Parameter(name = "name", description = "新的项目名称", required = true, example = ""),
        ]
    )
    @PostMapping("{projectId}/{name}")
    @ApiResponse(useReturnTypeSchema = true)
    fun updateName(
        @PathVariable("projectId") projectId: String,
        @PathVariable("name") name: String,
    ) = projectService.updateName(projectId, name)
}
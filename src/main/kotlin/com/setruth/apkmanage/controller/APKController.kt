package com.setruth.apkmanage.controller

import com.setruth.apkmanage.model.APKInfoEntity
import com.setruth.apkmanage.service.APKService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("apkManage")
@CrossOrigin
@Tag(name = "APK信息管理")
class APKController(
    private val apkService: APKService,
) {
    @PostMapping
    @Operation(summary = "新增APK信息")
    @ApiResponse(useReturnTypeSchema = true)
    fun addAPKInfo(@RequestBody newInfo: APKInfoEntity) = apkService.addAPKInfo(newInfo)

    @Operation(summary = "上传APK文件")
    @ApiResponse(useReturnTypeSchema = true)
    @PostMapping("upload/{projectId}")
    @Synchronized
    fun apkUpload(
        @PathVariable("projectId") projectId: String,
        @RequestParam("apkFile") file: MultipartFile,
    ) = apkService.uploadAPK(projectId, file)

    @GetMapping("newest/{projectId}")
    @Operation(summary = "获取项目最新APK信息")
    @ApiResponse(useReturnTypeSchema = true)
    fun newestAPKInfo(@PathVariable("projectId") projectId: String) = apkService.getNewestInfo(projectId)

    @GetMapping("{projectId}")
    @Operation(summary = "获取项目所有APK信息")
    @ApiResponse(useReturnTypeSchema = true)
    fun allAPKInfo(@PathVariable("projectId") projectId: String) = apkService.allAPKInfo(projectId)

    @GetMapping("download/{projectId}")
    @Operation(summary = "下载APK文件")
    @ApiResponse(useReturnTypeSchema = true)
    fun apkDownload(@PathVariable("projectId") projectId:String, response: HttpServletResponse) {
        apkService.apkDownload(projectId,response)
    }
}
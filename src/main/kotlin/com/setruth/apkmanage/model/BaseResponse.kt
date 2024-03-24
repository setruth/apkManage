package com.setruth.apkmanage.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

@Schema(description = "基类响应对象")
data class BaseResponse<T>(
    @Schema(description = "响应消息", name = "message", required = true, example = "成功")
    var msg: String = "请求成功",
    @Schema(description = "响应数据", name = "data")
    var data: T? = null, 
    )

infix fun <T> HttpStatus.response(response: BaseResponse<T>) = ResponseEntity(response, this)
package com.setruth.apkmanage.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.setruth.apkmanage.model.APKInfoEntity
import com.setruth.apkmanage.model.APK_PATH
import com.setruth.apkmanage.model.BaseResponse
import com.setruth.apkmanage.model.response
import com.setruth.apkmanage.repository.APKRepository
import com.setruth.apkmanage.utils.FileUtil
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.net.URLEncoder

/**
 *
 * @author setruth
 * @date 2024/3/23
 * @time 11:16
 */
@Service
class APKService(
    val apkRep: APKRepository,
) {
    fun addAPKInfo(newInfo: APKInfoEntity) = apkRep.addAPKInfo(newInfo).let {
        HttpStatus.OK response BaseResponse("新增成功", true)
    }

    fun getNewestInfo(projectId: String) = HttpStatus.OK response BaseResponse("获取成功", apkRep.newestInfo(projectId))

    fun allAPKInfo(projectId: String) = HttpStatus.OK response BaseResponse("获取成功", apkRep.allAPKInfo(projectId))

    fun uploadAPK(projectId: String, file: MultipartFile) = runBlocking {
        if (FileUtil.checkCreateFolder(APK_PATH)) {
            FileUtil.findFileInFolder(APK_PATH, projectId)?.delete()
        }
        FileUtil.saveFile(file, APK_PATH, "${projectId}.${file.originalFilename.toString().substringAfterLast(".")}")
        return@runBlocking HttpStatus.OK response BaseResponse("上传成功", true)
    }

    fun apkDownload(projectId: String, response: HttpServletResponse) =
        FileUtil.findFileInFolder(APK_PATH, projectId)?.let { file ->
            response.contentType = "application/force-download"
            response.addHeader("Content-Length", file.length().toString())
            response.addHeader("Content-Disposition", "attachment;fileName=${URLEncoder.encode(file.name, "UTF-8")}")
            val buffer = ByteArray(1024)
            FileInputStream(file).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    val os: OutputStream = response.outputStream
                    var i = bis.read(buffer)
                    while (i != -1) {
                        os.write(buffer, 0, i)
                        i = bis.read(buffer)
                    }
                }
            }
        } ?: with(response) {
            setHeader("Access-Control-Allow-Origin", "*");
            setHeader("Access-Control-Allow-Methods", "*");
            setHeader("Access-Control-Allow-Headers", "*");
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = "UTF-8"
            writer.write(ObjectMapper().writeValueAsString(BaseResponse(msg = "不存在文件", data = null)))
        }


}
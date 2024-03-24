package com.setruth.apkmanage.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.ceil

/**
 * TODO
 * @author setruth
 * @date 2023/11/3
 * @time 15:30
 */
object FileUtil {
    fun checkCreateFolder(folderPath: String):Boolean {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
            return false
        }
        return true
    }

    suspend fun saveFile(file: MultipartFile,path: String, fileName: String) = withContext(Dispatchers.IO) {
        checkCreateFolder(path)
        val jobs = mutableListOf<Job>()
        val blockCount = ceil(file.size.toDouble() / FILE_BLOCK_SIZE).toInt()
        val data = try {
            file.bytes
        } catch (e: IOException) {
            return@withContext false
        }
        val outputStream = FileOutputStream(
            File(
                path,
                fileName
            ), false
        )
        try {
            repeat(blockCount) {
                val startPos = it * FILE_BLOCK_SIZE
                val endPos = (startPos + FILE_BLOCK_SIZE).coerceAtMost(data.size)
                outputStream.write(data, startPos, endPos - startPos)
            }
        } catch (e: IOException) {
            outputStream.close()
            return@withContext false
        }
        outputStream.close()
        true
    }

    fun findFileInFolder(folderPath: String, fileName: String): File? {
        val folder = File(folderPath)
        if (!folder.exists() || !folder.isDirectory) {
            return null
        }
        for (file in folder.listFiles()!!) {
            if (file.isFile && file.nameWithoutExtension == fileName) {
                return file
            }
        }
        return null
    }

    fun newFileName(newName: String, fileName: String) =
        "${newName}.${fileName.substringAfterLast(".")}"
}
private const val FILE_BLOCK_SIZE = 1024 * 1024
package com.setruth.apkmanage.service

import com.setruth.apkmanage.model.APP_ID
import com.setruth.apkmanage.model.ProjectInfoEntity
import com.setruth.apkmanage.model.BaseResponse
import com.setruth.apkmanage.model.response
import com.setruth.apkmanage.repository.ProjectRepository
import com.setruth.apkmanage.utils.SnowflakeId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 *
 * @author setruth
 * @date 2024/3/23
 * @time 11:16
 */
@Service
class ProjectService(
    private val snowflakeId: SnowflakeId,
    private val projectRep: ProjectRepository,
) {
    fun projects() = HttpStatus.OK response BaseResponse("获取成功", projectRep.projects())

    fun add(projectName: String): ResponseEntity<BaseResponse<Boolean>> {
        projectRep.getInfoByName(projectName)?.let {
            return HttpStatus.CONFLICT response BaseResponse("项目名称已存在", false)
        }
        projectRep.add(ProjectInfoEntity(projectId = snowflakeId.generateId(APP_ID), name = projectName))
        return HttpStatus.OK response BaseResponse("新增成功", true)
    }

    fun del(projectId: String) = projectRep.del(projectId).run {
        HttpStatus.OK response BaseResponse("删除成功", true)
    }

    fun updateName(projectId: String, name: String) = projectRep.getProjectInfo(projectId)?.let{
        projectRep.getInfoByName(name)?.let {
            return HttpStatus.CONFLICT response BaseResponse("项目名称已存在", false)
        }
        projectRep.updateName(it.copy(name = name))
        HttpStatus.OK response BaseResponse("更新成功",true)
    }?: (HttpStatus.NOT_FOUND response BaseResponse("不存在的项目",false))

}
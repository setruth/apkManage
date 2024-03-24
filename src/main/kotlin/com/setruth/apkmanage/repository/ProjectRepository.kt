package com.setruth.apkmanage.repository

import com.mybatisflex.kotlin.extensions.db.baseMapper
import com.mybatisflex.kotlin.extensions.db.queryOne
import com.mybatisflex.kotlin.extensions.kproperty.eq
import com.mybatisflex.kotlin.extensions.mapper.all
import com.mybatisflex.kotlin.extensions.mapper.update
import com.mybatisflex.kotlin.extensions.wrapper.whereWith
import com.setruth.apkmanage.model.ProjectInfoEntity
import org.springframework.stereotype.Service

/**
 *
 * @author setruth
 * @date 2024/3/23
 * @time 11:16
 */
@Service
class ProjectRepository {
    fun projects() = ProjectInfoEntity::class.all

    fun del(projectId: String) = ProjectInfoEntity::class.baseMapper.deleteById(projectId)

    fun add(projectInfoEntity: ProjectInfoEntity) = projectInfoEntity.save()

    fun updateName(newInfo: ProjectInfoEntity) {
        newInfo.update {
            ProjectInfoEntity::projectId eq newInfo.projectId
        }
    }
    fun getProjectInfo(projectId: String) = queryOne<ProjectInfoEntity>{
        whereWith {
            ProjectInfoEntity::projectId eq projectId
        }
    }
    fun getInfoByName(name: String) =queryOne<ProjectInfoEntity>{
        whereWith {
            ProjectInfoEntity::name eq name
        }
    }

}
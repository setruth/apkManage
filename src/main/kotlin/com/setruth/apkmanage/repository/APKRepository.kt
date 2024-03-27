package com.setruth.apkmanage.repository

import com.mybatisflex.kotlin.extensions.db.baseMapper
import com.mybatisflex.kotlin.extensions.db.query
import com.mybatisflex.kotlin.extensions.db.queryOne
import com.mybatisflex.kotlin.extensions.kproperty.eq
import com.mybatisflex.kotlin.extensions.kproperty.unaryMinus
import com.mybatisflex.kotlin.extensions.mapper.deleteByQuery
import com.mybatisflex.kotlin.extensions.wrapper.whereWith
import com.setruth.apkmanage.model.APKInfoEntity
import org.springframework.stereotype.Service

/**
 *
 * @author setruth
 * @date 2024/3/23
 * @time 11:16
 */
@Service
class APKRepository {
    fun addAPKInfo(newInfo: APKInfoEntity) = newInfo.save()

    fun newestInfo(projectId: String) = queryOne<APKInfoEntity> {
        whereWith { APKInfoEntity::projectId eq projectId }
        orderBy(-APKInfoEntity::updateTime)
    }

    fun allAPKInfo(projectId: String) = query<APKInfoEntity> {
        whereWith { APKInfoEntity::projectId eq projectId }
        orderBy(-APKInfoEntity::updateTime)
    }
    fun delAPKInfo(projectId: String) = APKInfoEntity::class.baseMapper.deleteByQuery {
        whereWith {
            APKInfoEntity::projectId eq projectId
        }
    }
}
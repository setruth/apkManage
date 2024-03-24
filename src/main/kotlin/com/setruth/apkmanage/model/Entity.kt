package com.setruth.apkmanage.model

import com.gitee.sunchenbin.mybatis.actable.annotation.*
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant
import com.mybatisflex.annotation.Id
import com.mybatisflex.core.activerecord.MapperModel
import io.swagger.v3.oas.annotations.media.Schema

@com.mybatisflex.annotation.Table("apk_info")
@Table(name = "apk_info")
@TableCharset(MySqlCharsetConstant.UTF8)
@TableEngine(MySqlEngineConstant.InnoDB)
@Schema(description = "apk信息")
data class APKInfoEntity(
    @Column
    @Schema(description = "APK版本号")
    var version: String,
    @Column
    @Schema(description = "APK版本代码")
    var versionCode: Int,
    @Column
    @Schema(description = "APK更新内容")
    var updateContent: String,
    @Column
    @Schema(description = "上传时间")
        var updateTime: Long,
    @Schema(description = "对应项目ID")
    @Column
    var projectId: String?,
):MapperModel<APKInfoEntity>

@com.mybatisflex.annotation.Table("projects")
@Table(name = "projects")
@TableCharset(MySqlCharsetConstant.UTF8)
@TableEngine(MySqlEngineConstant.InnoDB)
@Schema(description = "项目信息")
data class ProjectInfoEntity(
    @Id
    @Column(isKey = true)
    @Schema(description = "对应项目ID,创建的时候为null")
    var projectId:String?,
    @Column
    @Schema(description = "项目名称")
    var name: String,
):MapperModel<ProjectInfoEntity>
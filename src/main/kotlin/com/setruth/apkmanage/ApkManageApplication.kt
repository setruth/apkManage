package com.setruth.apkmanage

import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandler
import com.mybatisflex.core.BaseMapper
import com.mybatisflex.kotlin.scope.runFlex
import org.apache.ibatis.io.ResolverUtil
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import javax.sql.DataSource

@SpringBootApplication
@MapperScan(value = ["com.gitee.sunchenbin.mybatis.actable.dao.*","com.setruth"])
@ComponentScan("com.gitee.sunchenbin.mybatis.actable.manager.*")
class ApkManageApplication

fun main(args: Array<String>) {
    runApplication<ApkManageApplication>(*args)
}
@Component
class CommandLineRunnerImpl(
    private val startUpHandler: StartUpHandler,
    private val dataSource: DataSource
) : CommandLineRunner {
    override fun run(vararg args: String) {
        startUpHandler.startHandler()
        val resolverUtil = ResolverUtil<BaseMapper<*>>().apply {
            find(ResolverUtil.IsA(BaseMapper::class.java), "com.setruth")
        }
        runFlex { mybatisFlexBootstrap ->
            +dataSource
            resolverUtil.classes.forEach { mapperClass ->
                mybatisFlexBootstrap.addMapper(mapperClass)
            }
        }
    }
}
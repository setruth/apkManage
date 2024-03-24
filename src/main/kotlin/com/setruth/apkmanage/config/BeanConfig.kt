package com.setruth.apkmanage.config

import com.setruth.apkmanage.utils.SnowflakeId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author setruth
 * @date 2024/3/23
 * @time 11:12
 */
@Configuration
class BeanConfig {
    @Bean
    fun createSnowflakeId() = SnowflakeId()
}
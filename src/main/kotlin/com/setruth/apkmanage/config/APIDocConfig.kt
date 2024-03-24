package com.setruth.apkmanage.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * TODO
 * @author setruth
 * @date 2023/12/15
 * @time 16:16
 */
@Configuration
class APIDocConfig {
    private fun license() = License().apply {
        name = "MIT"
        url = "https://opensource.org/licenses/MIT"
    }

    private fun info(): Info {
        return Info()
            .contact(Contact().apply {
                name = "Setruth"
                email = "1607908758@qq.com"
            })
            .title("APK管理")
            .version("v0.1")
            .license(license())
    }


    @Bean
    fun springShopOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(info())

    }
}
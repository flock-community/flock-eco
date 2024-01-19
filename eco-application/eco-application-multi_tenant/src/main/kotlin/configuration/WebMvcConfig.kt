package community.flock.eco.application.multitenant.config

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class WebMvcConfig() : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/ui/**")
            .addResourceLocations("/index.html", "classpath:/index.html")
            .addResourceLocations("/main.js", "classpath:/main.js")
            .addResourceLocations("/donation.html", "classpath:/donation.html")
    }
}

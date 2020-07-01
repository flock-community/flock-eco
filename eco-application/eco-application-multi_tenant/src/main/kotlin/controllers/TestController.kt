package community.flock.eco.application.multi_tenant.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/_ah")
class TestController {

    @GetMapping("/health")
    fun health(): String {
        return "OK"
    }

}

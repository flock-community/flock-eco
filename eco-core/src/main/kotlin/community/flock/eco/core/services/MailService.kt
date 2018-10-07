package community.flock.eco.core.services

import java.security.Principal

interface MailService {

    fun sendMail(principal: Principal)

}

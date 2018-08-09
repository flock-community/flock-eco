package community.flock.eco.feature.payments.authorities

import community.flock.eco.core.authorities.Authority


enum class PaymentAuthority: Authority {
    READ,
    WRITE
}
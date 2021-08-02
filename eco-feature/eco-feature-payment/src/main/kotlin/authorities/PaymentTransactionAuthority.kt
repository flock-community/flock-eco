package community.flock.eco.feature.payment.authorities

import community.flock.eco.core.authorities.Authority

enum class PaymentTransactionAuthority : Authority {
    READ,
    WRITE
}

package community.flock.eco.feature.payment.services

import community.flock.eco.feature.payment.model.PaymentTransaction
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

@Service
class PaymentSepaXmlService {

    data class Sepa(
        val id: String,
        val privateIdentification: String,
        val message: String,
        val creationDatetime: LocalDateTime = LocalDateTime.now(),
        val collectionDateTime: LocalDateTime,
        val organisation: SepaOrganisation,
        val transactions: List<PaymentTransaction>
    )

    data class SepaOrganisation(
        val name: String,
        val iban: String,
        val bic: String,
        val country: SepaCountry,
        val address1: String,
        val address2: String
    )

    enum class SepaCountry {
        FI,
        AT,
        PT,
        BE,
        BG,
        ES,
        HR,
        CY,
        CZ,
        DK,
        EE,
        FR,
        GF,
        DE,
        GI,
        GR,
        GP,
        GG,
        HU,
        IS,
        IE,
        IM,
        IT,
        JE,
        LV,
        LI,
        LT,
        LU,
        MT,
        MQ,
        YT,
        MC,
        NL,
        NO,
        PL,
        RE,
        RO,
        BL,
        MF,
        PM,
        SM,
        SK,
        SI,
        SE,
        CH,
        GB
    }

    fun generate(sepa: Sepa): Document {
        val count = sepa.transactions.size.toString()
        val total = sepa.transactions.map { it.amount }.reduce { acc, cur -> acc + cur }.toString()

        return document {

            element("Document") {
                attribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02")
                attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")

                element("CstmrDrctDbtInitn") {

                    element("GrpHdr") {
                        element("MsgId", sepa.id)
                        element("CreDtTm", sepa.creationDatetime.format(DateTimeFormatter.ISO_DATE_TIME))
                        element("NbOfTxs", count)
                        element("CtrlSum", total)
                        element("InitgPty") {
                            element("Nm", sepa.organisation.name)
                        }
                    }

                    element("PmtInf") {
                        element("PmtInfId", sepa.id)
                        element("PmtMtd", "DD")
                        element("BtchBookg", "true")
                        element("NbOfTxs", count)
                        element("CtrlSum", total)

                        element("PmtTpInf") {
                            element("SvcLvl") {
                                element("Cd", "SEPA")
                            }
                            element("LclInstrm") {
                                element("Cd", "CORE")
                            }
                            element("SeqTp", "RCUR")
                        }

                        element("ReqdColltnDt", sepa.collectionDateTime.format(DateTimeFormatter.ISO_DATE))

                        element("Cdtr") {
                            element("Nm", sepa.organisation.name)
                            element("PstlAdr") {
                                element("Ctry", sepa.organisation.country.toString())
                                element("AdrLine", sepa.organisation.address1)
                                element("AdrLine", sepa.organisation.address2)
                            }
                        }

                        element("CdtrAcct") {
                            element("Id") {
                                element("IBAN", sepa.organisation.iban)
                            }
                        }

                        element("CdtrAgt") {
                            element("FinInstnId") {
                                element("BIC", sepa.organisation.bic)
                            }
                        }

                        element("ChrgBr", "SLEV")

                        sepa.transactions.forEachIndexed { index, transaction ->
                            element("DrctDbtTxInf") {
                                element("PmtId") {
                                    element("EndToEndId", "${transaction.created.format(DateTimeFormatter.BASIC_ISO_DATE)}-$index")
                                }
                                element("InstdAmt") {
                                    attribute("Ccy", "EUR")
                                    value(transaction.amount.toString())
                                }

                                element("DrctDbtTx") {
                                    element("MndtRltdInf") {
                                        element("MndtId", transaction.mandate.code.replace("-", ""))
                                        element("DtOfSgntr", transaction.mandate.startDate.format(DateTimeFormatter.ISO_DATE))
                                        element("AmdmntInd", "false")
                                    }
                                    element("CdtrSchmeId") {
                                        element("Id") {
                                            element("PrvtId") {
                                                element("Othr") {
                                                    element("Id", sepa.privateIdentification)
                                                    element("SchmeNm") {
                                                        element("Prtry", "SEPA")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                transaction.mandate.bankAccount?.let { bankAccount ->
                                    element("DbtrAgt") {
                                        element("FinInstnId") {
                                            element("BIC", bankAccount.bic)
                                        }
                                    }

                                    element("Dbtr") {
                                        element("Nm", bankAccount.name)
                                        element("PstlAdr") {
                                            element(
                                                "Ctry",
                                                bankAccount.country.let {
                                                    SepaCountry.valueOf(it).toString()
                                                }
                                            )
                                        }
                                    }

                                    element("DbtrAcct") {
                                        element("Id") {
                                            element("IBAN", bankAccount.iban)
                                        }
                                    }
                                }

                                element("Purp") {
                                    element("Cd", "OTHR")
                                }

                                element("RmtInf") {
                                    element("Ustrd", sepa.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun document(init: Document.() -> Document): Document = DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .newDocument()
        .init()

    private fun Document.element(name: String, init: Element.() -> Unit): Document = apply {
        createElement(name)
            .also { appendChild(it) }
            .init()
    }

    private fun Element.element(name: String, init: Element.() -> Unit) {
        ownerDocument.createElement(name)
            .also { appendChild(it) }
            .init()
    }

    private fun Element.element(name: String, value: String) {
        ownerDocument.createElement(name)
            .also { it.appendChild(ownerDocument.createTextNode(value)) }
            .also { appendChild(it) }
    }

    private fun Element.attribute(name: String, value: String) {
        setAttribute(name, value)
    }

    private fun Element.value(value: String) {
        appendChild(ownerDocument.createTextNode(value))
    }
}

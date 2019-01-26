package community.flock.eco.feature.payment.services;

import community.flock.eco.feature.payment.model.PaymentTransaction
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLStreamWriter

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
        NL
    }

    fun generate(sepa: Sepa): Document {

        val count = sepa.transactions.size.toString()
        val total = sepa.transactions
                .map { it.amount }
                .reduce({ acc, cur -> acc + cur })
                .toString()

        return document {

            it.element("Document") {
                it.attribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02")
                it.attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")

                it.element("CstmrDrctDbtInitn") {

                    it.element("GrpHdr") {
                        it.element("MsgId", sepa.id)
                        it.element("CreDtTm", sepa.creationDatetime.format(DateTimeFormatter.ISO_DATE_TIME))
                        it.element("NbOfTxs", count)
                        it.element("CtrlSum", total)
                        it.element("InitgPty") {
                            it.element("Nm", sepa.organisation.name)
                        }
                    }

                    it.element("PmtInf") {
                        it.element("PmtInfId", sepa.id)
                        it.element("PmtMtd", "DD")
                        it.element("BtchBookg", "true")
                        it.element("NbOfTxs", count)
                        it.element("CtrlSum", total)

                        it.element("PmtTpInf") {
                            it.element("SvcLvl") {
                                it.element("Cd", "SEPA")
                            }
                            it.element("LclInstrm") {
                                it.element("Cd", "CORE")
                            }
                            it.element("SeqTp", "RCUR")

                        }

                        it.element("ReqdColltnDt", sepa.collectionDateTime.format(DateTimeFormatter.ISO_DATE))

                        it.element("Cdtr") {
                            it.element("Nm", sepa.organisation.name)
                            it.element("PstlAdr") {
                                it.element("Ctry", sepa.organisation.country.toString())
                                it.element("AdrLine", sepa.organisation.address1)
                                it.element("AdrLine", sepa.organisation.address2)
                            }
                        }

                        it.element("CdtrAcct") {
                            it.element("Id") {
                                it.element("IBAN", sepa.organisation.iban)
                            }
                        }

                        it.element("CdtrAgt") {
                            it.element("FinInstnId") {
                                it.element("BIC", sepa.organisation.bic)
                            }
                        }

                        it.element("ChrgBr", "SLEV")

                        sepa.transactions.forEachIndexed { index, transaction ->
                            it.element("DrctDbtTxInf") {
                                it.element("PmtId") {
                                    it.element("EndToEndId", "${transaction.created.format(DateTimeFormatter.BASIC_ISO_DATE)}-$index")
                                }
                                it.element("InstdAmt") {
                                    it.attribute("Ccy", "EUR")
                                    it.value(transaction.amount.toString())
                                }

                                it.element("DrctDbtTx") {
                                    it.element("MndtRltdInf") {
                                        it.element("MndtId", transaction.mandate.code.replace("-", ""))
                                        it.element("DtOfSgntr", transaction.mandate.startDate.format(DateTimeFormatter.ISO_DATE))
                                        it.element("AmdmntInd", "false")
                                    }
                                    it.element("CdtrSchmeId") {
                                        it.element("Id") {
                                            it.element("PrvtId") {
                                                it.element("Othr") {
                                                    it.element("Id", sepa.privateIdentification)
                                                    it.element("SchmeNm") {
                                                        it.element("Prtry", "SEPA")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                transaction.mandate.bankAccount?.let { bankAccount ->
                                    it.element("DbtrAgt") {
                                        it.element("FinInstnId") {
                                            it.element("BIC", bankAccount.bic)
                                        }
                                    }

                                    it.element("Dbtr") {
                                        it.element("Nm", bankAccount.name)
                                        it.element("PstlAdr") {
                                            it.element("Ctry", bankAccount.country.let {
                                                SepaCountry.valueOf(it).toString()
                                            })
                                        }
                                    }

                                    it.element("DbtrAcct") {
                                        it.element("Id") {
                                            it.element("IBAN", bankAccount.iban)
                                        }
                                    }
                                }

                                it.element("Purp") {
                                    it.element("Cd", "OTHR")
                                }

                                it.element("RmtInf") {
                                    it.element("Ustrd", sepa.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    fun XMLStreamWriter.document(init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
        this.writeStartDocument("UTF-8", "1.0")
        this.init()
        this.writeEndDocument()
        return this
    }

    fun XMLStreamWriter.element(name: String, init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
        this.writeStartElement(name)
        this.init()
        this.writeEndElement()
        return this
    }

    fun XMLStreamWriter.element(name: String, content: String) {
        element(name) {
            writeCharacters(content)
        }
    }

    fun XMLStreamWriter.attribute(name: String, value: String) = writeAttribute(name, value)

    fun XMLStreamWriter.value(content: String) = writeCharacters(content)


    fun document(init: (doc: Document) -> Unit): Document {
        val documentFactory = DocumentBuilderFactory.newInstance();
        val documentBuilder = documentFactory.newDocumentBuilder();
        val doc = documentBuilder.newDocument()
        init(doc)
        return doc;
    }


    fun Document.element(name: String, init: (Element) -> Unit) {
        val node = this.createElement(name)
        this.appendChild(node)
        init(node)
    }

    fun Document.elementNS(namespaceURI: String, name: String, init: (Element) -> Unit) {
        val node = this.createElementNS(namespaceURI, name)
        this.appendChild(node)
        init(node)
    }

    fun Element.element(name: String, init: (Element) -> Unit) {
        val node = this.ownerDocument.createElement(name)
        this.appendChild(node)
        init(node)
    }

    fun Element.element(name: String, value: String) {
        val node = this.ownerDocument.createElement(name)
        node.appendChild(this.ownerDocument.createTextNode(value))
        this.appendChild(node)
    }

    fun Element.attribute(name: String, value: String) {
        this.setAttribute(name, value)
    }

    fun Element.value(value: String) {
        this.appendChild(this.ownerDocument.createTextNode(value))
    }

}
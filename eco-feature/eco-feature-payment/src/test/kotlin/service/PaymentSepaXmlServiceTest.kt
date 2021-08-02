package community.flock.eco.feature.payment.service

import community.flock.eco.feature.payment.PaymentConfiguration
import community.flock.eco.feature.payment.model.*
import community.flock.eco.feature.payment.services.PaymentSepaXmlService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.io.StringWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import javax.transaction.Transactional
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@SpringBootTest(classes = [PaymentConfiguration::class])
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
class PaymentSepaXmlServiceTest(
    @Autowired private val paymentSepaXmlService: PaymentSepaXmlService
) {

    val date: LocalDate = LocalDate.of(2019, 1, 1)
    val data = listOf(
        PaymentTransaction(
            amount = 5.00,
            mandate = PaymentMandate(
                code = "2",
                amount = 5.00,
                frequency = PaymentFrequency.MONTHLY,
                type = PaymentType.SEPA,
                bankAccount = PaymentBankAccount(
                    name = "H. van Broektest",
                    iban = "NL97ZZZ342160180000",
                    bic = "TRIONL2U",
                    country = "NL"
                ),
                startDate = date
            ),
            created = date
        ),
        PaymentTransaction(
            amount = 5.00,
            mandate = PaymentMandate(
                code = "256",
                amount = 5.00,
                frequency = PaymentFrequency.MONTHLY,
                type = PaymentType.SEPA,
                bankAccount = PaymentBankAccount(
                    name = "Mw H Testoeven",
                    iban = "NL00TRIO0000000",
                    bic = "TRIONL2U",
                    country = "NL"
                ),
                startDate = date
            ),
            created = date
        )
    )

    @Test
    fun test() {
        val sepa = PaymentSepaXmlService.Sepa(
            id = "90000000092",
            message = "HARTELIJK DANK VOOR UW BIJDRAGE",
            privateIdentification = "NL00ZZZ0000000000",
            organisation = PaymentSepaXmlService.SepaOrganisation(
                name = "Doneasy",
                iban = "NL00XXXX0000000000",
                bic = "XXXXNL2A",
                address1 = "Address 1",
                address2 = "Address 2",
                country = PaymentSepaXmlService.SepaCountry.NL
            ),
            creationDatetime = LocalDateTime.of(2019, Month.JANUARY, 15, 0, 0),
            collectionDateTime = LocalDateTime.of(2019, Month.JANUARY, 28, 0, 0),
            transactions = data
        )

        val doc = paymentSepaXmlService.generate(sepa)

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

        val writer = StringWriter()
        transformer.transform(DOMSource(doc), StreamResult(writer))
        val output = writer.buffer.toString()

        val example = javaClass.classLoader.getResource("example_sepa.xml").readText()

        assertEquals(example, output)
    }
}

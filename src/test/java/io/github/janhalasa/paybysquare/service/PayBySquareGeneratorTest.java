package io.github.janhalasa.paybysquare.service;

import io.github.janhalasa.paybysquare.model.BankAccount;
import io.github.janhalasa.paybysquare.model.DirectDebit;
import io.github.janhalasa.paybysquare.model.DirectDebitScheme;
import io.github.janhalasa.paybysquare.model.DirectDebitType;
import io.github.janhalasa.paybysquare.model.MonthClassifier;
import io.github.janhalasa.paybysquare.model.PayBySquareDocument;
import io.github.janhalasa.paybysquare.model.Payment;
import io.github.janhalasa.paybysquare.model.PaymentOption;
import io.github.janhalasa.paybysquare.model.Periodicity;
import io.github.janhalasa.paybysquare.model.StandingOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PayBySquareGeneratorTest {

    @Test
    public void givenAllOptionsSet_whenGenerate_thenQrCodeGenerated() throws Exception {
        // 1. Create the Document
        PayBySquareDocument doc = new PayBySquareDocument();
        doc.setInvoiceId("INV-2023-001");
        doc.setBeneficiaryName("Ján Halaša");
        doc.setBeneficiaryAddress1("P. O. Hviezdoslava 843");
        doc.setBeneficiaryAddress2("Vyšný Kubín, Slovensko");

        // 2. Create a Payment
        Payment payment = new Payment();
        payment.setPaymentOptions(PaymentOption.PAYMENT_ORDER); // This is the default value, not necessary to be set
        payment.setAmount(new BigDecimal("123.45"));
        payment.setCurrencyCode(Payment.CURRENCY_EUR); // This is the default value, not necessary to be set
        payment.setPaymentDueDate(LocalDate.now());
        payment.setVariableSymbol("1234567890");
        payment.setConstantSymbol("0008");
        payment.setSpecificSymbol("9999");
        // payment.setOriginatorsReference("/VS12345/SS9999/KS0008");
        payment.setPaymentNote("Payment for Invoice 001 - Full Service");

        // 3. Add Bank Account (IBAN & BIC)
        BankAccount account = new BankAccount();
        account.setIban("SK3883300000002503144937");
        account.setBic("FIOZSKBAXXX");
        payment.addBankAccount(account);

        // 4. (Optional) Add Standing Order details
        StandingOrder so = new StandingOrder();
        so.setDay(15);
        so.setMonth(MonthClassifier.APRIL);
        so.setPeriodicity(Periodicity.MONTHLY);
        so.setLastDate(LocalDate.of(2025, 12, 31)); // Dec 31, 2025
        payment.setStandingOrder(so);

        // 5. (Optional) Add Direct Debit details
        DirectDebit dd = new DirectDebit();
        dd.setDirectDebitScheme(DirectDebitScheme.SEPA);
        dd.setDirectDebitType(DirectDebitType.ONE_OFF);
        dd.setVariableSymbol("1234567890");
        dd.setSpecificSymbol("1111");
        dd.setOriginatorsReference("MANDATE-001");
        dd.setMandateID("MND-102030");
        dd.setCreditorID("CID-998877");
        dd.setContractID("CTR-554433");
        dd.setMaxAmount(new BigDecimal(500));
        dd.setValidTillDate(LocalDate.of(2030, 1, 1));
        payment.setDirectDebit(dd);

        // Add the payment to the document
        doc.addPayment(payment);

        // 6. Generate the QR Code
        PayBySquareGenerator generator = new PayBySquareGenerator();

        // Generate raw PayBySquare string (LZMA compressed, Base32hex encoded)
        String stringCode = generator.generateString(doc);
        assertNotNull(stringCode);

        // Generate PNG image
        byte[] qrImage = generator.generateQrCode(doc, 256); // 256x256 pixels
        assertNotNull(qrImage);
        // System.out.println("PayBySquare String: " + stringCode);
        // java.nio.file.Files.write(java.nio.file.Path.of("paybysquare.png"), qrImage);
    }
}

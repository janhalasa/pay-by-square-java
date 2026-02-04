package io.github.janhalasa.paybysquare.service;

import io.github.janhalasa.paybysquare.model.BankAccount;
import io.github.janhalasa.paybysquare.model.DirectDebit;
import io.github.janhalasa.paybysquare.model.PayBySquareDocument;
import io.github.janhalasa.paybysquare.model.Payment;
import io.github.janhalasa.paybysquare.model.StandingOrder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PayBySquareGeneratorTest {

    @Test
    public void givenAllOptionsSet_whenGenerate_thenQrCodeGenerated() throws Exception {
        // 1. Create the Document
        PayBySquareDocument doc = new PayBySquareDocument();
        doc.setInvoiceId("INV-2023-001");
        doc.setBeneficiaryName("Ján Halaša");
        doc.setBeneficiaryAddress1("Main Street 1");
        doc.setBeneficiaryAddress2("Bratislava, Slovakia");

        // 2. Create a Payment
        Payment payment = new Payment();
        payment.setPaymentOptions(1); // Standard payment
        payment.setAmount(123.45);
        payment.setCurrencyCode("EUR");
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
        so.setMonth(1); // Every month (1) or specific month? Check standard. Usually interval.
        so.setPeriodicity("M"); // Monthly
        so.setLastDate(LocalDate.of(2025, 12, 31)); // Dec 31, 2025
        payment.setStandingOrder(so);

        // 5. (Optional) Add Direct Debit details
        DirectDebit dd = new DirectDebit();
        dd.setDirectDebitScheme(1);
        dd.setDirectDebitType(1);
        dd.setVariableSymbol("1234567890");
        dd.setSpecificSymbol("1111");
        dd.setOriginatorsReference("MANDATE-001");
        dd.setMandateID("MND-102030");
        dd.setCreditorID("CID-998877");
        dd.setContractID("CTR-554433");
        dd.setMaxAmount(500.00);
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
        java.nio.file.Files.write(java.nio.file.Path.of("paybysquare.png"), qrImage);
    }
}

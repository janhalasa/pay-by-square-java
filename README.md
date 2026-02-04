# PayBySquare Java Library

A Java library for generating PayBySquare QR codes according to the Slovak PayBySquare standard v1.1.0.

## Overview
This library provides a full implementation of the PayBySquare standard, including:
1.  **Data Modeling**: POJOs for the PayBySquare schema.
2.  **Serialization**: Conversion to the required tab-separated format.
3.  **Encoding**: CRC32 checksum, LZMA compression (with specific parameters), bit-stuffing, and Base32hex encoding.
4.  **QR Generation**: Creating the final QR code image using ZXing.

## Installation

Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- PayBySquare Library (assuming it's built/installed locally for now) -->
    <dependency>
        <groupId>io.github.janhalasa</groupId>
        <artifactId>pay-by-square-java</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

The library is tiny, around 15 kB.

## Usage

Here is a comprehensive example showing how to populate all fields of a `PayBySquareDocument`, including optional symbols, notes, standing orders, and direct debits.

```java

import io.github.janhalasa.paybysquare.model.BankAccount;
import io.github.janhalasa.paybysquare.model.DirectDebit;
import io.github.janhalasa.paybysquare.model.PayBySquareDocument;
import io.github.janhalasa.paybysquare.model.Payment;
import io.github.janhalasa.paybysquare.model.StandingOrder;
import io.github.janhalasa.paybysquare.service.PayBySquareGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.GregorianCalendar;

public class Example {
    public static void main(String[] args) throws Exception {
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
        payment.setPaymentDueDate(new Date()); // Today
        payment.setVariableSymbol("1234567890");
        payment.setConstantSymbol("0308");
        payment.setSpecificSymbol("9999");
        payment.setOriginatorsReference("REF-123-ABC");
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
        so.setLastDate(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime()); // Dec 31, 2025
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
        dd.setValidTillDate(new GregorianCalendar(2030, Calendar.JANUARY, 1).getTime());
        payment.setDirectDebit(dd);

        // Add the payment to the document
        doc.addPayment(payment);

        // 6. Generate the QR Code
        PayBySquareGenerator generator = new PayBySquareGenerator();

        // Generate raw PayBySquare string (LZMA compressed, Base32hex encoded)
        String stringCode = generator.generateString(doc);
        System.out.println("PayBySquare String: " + stringCode);

        // Generate PNG image
        byte[] qrImage = generator.generateQrCode(doc, 256); // 256x256 pixels
        Files.write(Path.of("paybysquare.png"), qrImage);
        System.out.println("QR Code saved to paybysquare.png");
    }
}
```

## Key Components

- **`PayBySquareDocument`**: The root object model.
- **`PayBySquareSerializer`**: Converts the object model into the flat TSV string format.
- **`PayBySquareEncoder`**: Handles CRC32, LZMA compression, custom header construction, and Base32hex encoding.
- **`PayBySquareGenerator`**: The main entry point for generating QR codes.

## Standard Compliance
- **Version**: [1.1.0](https://www.sbaonline.sk/wp-content/uploads/2020/03/pay-by-square-specifications-1_1_0.pdf)
- **Compression**: LZMA (LC=3, LP=0, PB=2, Dictionary=128KB)
- **Checksum**: CRC32 (IEEE 802.3)
- **Encoding**: Base32hex
- **QR Code**: ISO/IEC 18004:2006, Alphanumeric Mode, Error Correction Level L

## License
MIT

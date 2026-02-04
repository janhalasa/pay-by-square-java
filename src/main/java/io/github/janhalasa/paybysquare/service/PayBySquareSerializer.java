package io.github.janhalasa.paybysquare.service;

import io.github.janhalasa.paybysquare.model.BankAccount;
import io.github.janhalasa.paybysquare.model.DirectDebit;
import io.github.janhalasa.paybysquare.model.PayBySquareDocument;
import io.github.janhalasa.paybysquare.model.Payment;
import io.github.janhalasa.paybysquare.model.StandingOrder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PayBySquareSerializer {

    private static final String SEPARATOR = "\t";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE; // YYYYMMDD format
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMAT = new DecimalFormat("0.##", symbols); // Max 2 decimal places for amount
        // Note: Spec says 9 decimal places for generic decimal, but amounts usually 2.
        // We will output as is but with dot separator.
        DECIMAL_FORMAT.setMaximumFractionDigits(9);
        DECIMAL_FORMAT.setGroupingUsed(false);
    }

    public String serialize(PayBySquareDocument document) {
        StringBuilder sb = new StringBuilder();

        // 1. InvoiceID (String, 0..1)
        appendString(sb, document.getInvoiceId());

        // 2. PaymentsCount (Int, 1..1)
        List<Payment> payments = document.getPayments();
        int count = payments != null ? payments.size() : 0;
        appendInt(sb, count);

        // List of Payments
        if (payments != null) {
            for (Payment payment : payments) {
                serializePayment(sb, payment);
            }
        }

        // Global Beneficiary Fields
        // 31. BeneficiaryName (String, 1..1) - In our model it is in document
        appendString(sb, document.getBeneficiaryName());
        // 32. BeneficiaryAddress1 (String, 0..1)
        appendString(sb, document.getBeneficiaryAddress1());
        // 33. BeneficiaryAddress2 (String, 0..1)
        appendString(sb, document.getBeneficiaryAddress2());

        return sb.toString();
    }

    private void serializePayment(StringBuilder sb, Payment payment) {
        // 3. PaymentOptions (Int, 1..1)
        appendInt(sb, payment.getPaymentOptions());

        // 4. Amount (Decimal, 1..1)
        appendDecimal(sb, payment.getAmount());

        // 5. CurrencyCode (String, 1..1)
        appendString(sb, payment.getCurrencyCode());

        // 6. PaymentDueDate (Date, 1..1)
        appendDate(sb, payment.getPaymentDueDate());

        // 7. VariableSymbol (String, 0..1)
        appendString(sb, payment.getVariableSymbol());

        // 8. ConstantSymbol (String, 0..1)
        appendString(sb, payment.getConstantSymbol());

        // 9. SpecificSymbol (String, 0..1)
        appendString(sb, payment.getSpecificSymbol());

        // 10. OriginatorsReference (String, 0..1)
        appendString(sb, payment.getOriginatorsReference());

        // 11. PaymentNote (String, 0..1)
        appendString(sb, payment.getPaymentNote());

        // 12. BankAccountsCount (Int, 1..1)
        List<BankAccount> bankAccounts = payment.getBankAccounts();
        int count = bankAccounts != null ? bankAccounts.size() : 0;
        appendInt(sb, count);

        // List of BankAccounts
        if (bankAccounts != null) {
            for (BankAccount account : bankAccounts) {
                serializeBankAccount(sb, account);
            }
        }

        // 15. StandingOrderExt (Boolean, 1..1)
        StandingOrder so = payment.getStandingOrder();
        appendBoolean(sb, so != null);
        if (so != null) {
            serializeStandingOrder(sb, so);
        }

        // 20. DirectDebitExt (Boolean, 1..1)
        DirectDebit dd = payment.getDirectDebit();
        appendBoolean(sb, dd != null);
        if (dd != null) {
            serializeDirectDebit(sb, dd);
        }
    }

    private void serializeBankAccount(StringBuilder sb, BankAccount account) {
        // 13. IBAN (String, 1..1)
        appendString(sb, account.getIban());
        // 14. BIC (String, 1..1)
        appendString(sb, account.getBic());
    }

    private void serializeStandingOrder(StringBuilder sb, StandingOrder so) {
        // 16. Day (Int, 1..1)
        appendInt(sb, so.getDay());
        // 17. Month (Int, 1..1)
        appendInt(sb, so.getMonth());
        // 18. Periodicity (String, 1..1)
        appendString(sb, so.getPeriodicity());
        // 19. LastDate (Date, 1..1)
        appendDate(sb, so.getLastDate());
    }

    private void serializeDirectDebit(StringBuilder sb, DirectDebit dd) {
        // 21. DirectDebitScheme (Int, 1..1)
        appendInt(sb, dd.getDirectDebitScheme());
        // 22. DirectDebitType (Int, 1..1)
        appendInt(sb, dd.getDirectDebitType());
        // 23. VariableSymbol (String, 0..1)
        appendString(sb, dd.getVariableSymbol());
        // 24. SpecificSymbol (String, 0..1)
        appendString(sb, dd.getSpecificSymbol());
        // 25. OriginatorsReference (String, 0..1)
        appendString(sb, dd.getOriginatorsReference());
        // 26. MandateID (String, 0..1)
        appendString(sb, dd.getMandateID());
        // 27. CreditorID (String, 0..1)
        appendString(sb, dd.getCreditorID());
        // 28. ContractID (String, 0..1)
        appendString(sb, dd.getContractID());
        // 29. MaxAmount (Decimal, 0..1)
        appendDecimal(sb, dd.getMaxAmount());
        // 30. ValidTillDate (Date, 0..1)
        appendDate(sb, dd.getValidTillDate());
    }

    private void appendString(StringBuilder sb, String value) {
        if (value != null) {
            sb.append(value);
        }
        sb.append(SEPARATOR);
    }

    private void appendInt(StringBuilder sb, Integer value) {
        if (value != null) {
            sb.append(value);
        }
        sb.append(SEPARATOR);
    }

    // Spec says Boolean is 1 or 0
    private void appendBoolean(StringBuilder sb, boolean value) {
        sb.append(value ? "1" : "0");
        sb.append(SEPARATOR);
    }

    private void appendDecimal(StringBuilder sb, Double value) {
        if (value != null) {
            sb.append(DECIMAL_FORMAT.format(value));
        }
        sb.append(SEPARATOR);
    }

    private void appendDate(StringBuilder sb, LocalDate value) {
        if (value != null) {
            sb.append(DATE_FORMATTER.format(value));
        }
        sb.append(SEPARATOR);
    }
}

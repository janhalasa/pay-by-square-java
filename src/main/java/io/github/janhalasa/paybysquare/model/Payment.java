package io.github.janhalasa.paybysquare.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Payment {
    private Integer paymentOptions = 1; // Default
    private Double amount;
    private String currencyCode;
    private Date paymentDueDate;
    private String variableSymbol;
    private String constantSymbol;
    private String specificSymbol;
    private String originatorsReference;
    private String paymentNote;
    private List<BankAccount> bankAccounts = new ArrayList<>();
    private StandingOrder standingOrder;
    private DirectDebit directDebit;

    public Integer getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(Integer paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public String getConstantSymbol() {
        return constantSymbol;
    }

    public void setConstantSymbol(String constantSymbol) {
        this.constantSymbol = constantSymbol;
    }

    public String getSpecificSymbol() {
        return specificSymbol;
    }

    public void setSpecificSymbol(String specificSymbol) {
        this.specificSymbol = specificSymbol;
    }

    public String getOriginatorsReference() {
        return originatorsReference;
    }

    public void setOriginatorsReference(String originatorsReference) {
        this.originatorsReference = originatorsReference;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void addBankAccount(BankAccount bankAccount) {
        this.bankAccounts.add(bankAccount);
    }

    public StandingOrder getStandingOrder() {
        return standingOrder;
    }

    public void setStandingOrder(StandingOrder standingOrder) {
        this.standingOrder = standingOrder;
    }

    public DirectDebit getDirectDebit() {
        return directDebit;
    }

    public void setDirectDebit(DirectDebit directDebit) {
        this.directDebit = directDebit;
    }
}

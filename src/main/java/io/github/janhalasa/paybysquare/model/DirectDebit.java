package io.github.janhalasa.paybysquare.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DirectDebit {
    private DirectDebitScheme directDebitScheme;
    private DirectDebitType directDebitType;
    private String variableSymbol;
    private String specificSymbol;
    private String originatorsReference;
    private String mandateID;
    private String creditorID;
    private String contractID;
    private BigDecimal maxAmount;
    private LocalDate validTillDate;

    public DirectDebitScheme getDirectDebitScheme() {
        return directDebitScheme;
    }

    public void setDirectDebitScheme(DirectDebitScheme directDebitScheme) {
        this.directDebitScheme = directDebitScheme;
    }

    public DirectDebitType getDirectDebitType() {
        return directDebitType;
    }

    public void setDirectDebitType(DirectDebitType directDebitType) {
        this.directDebitType = directDebitType;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
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

    public String getMandateID() {
        return mandateID;
    }

    public void setMandateID(String mandateID) {
        this.mandateID = mandateID;
    }

    public String getCreditorID() {
        return creditorID;
    }

    public void setCreditorID(String creditorID) {
        this.creditorID = creditorID;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public LocalDate getValidTillDate() {
        return validTillDate;
    }

    public void setValidTillDate(LocalDate validTillDate) {
        this.validTillDate = validTillDate;
    }
}

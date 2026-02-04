package io.github.janhalasa.paybysquare.model;

import java.util.Date;

public class DirectDebit {
    private Integer directDebitScheme;
    private Integer directDebitType;
    private String variableSymbol;
    private String specificSymbol;
    private String originatorsReference;
    private String mandateID;
    private String creditorID;
    private String contractID;
    private Double maxAmount;
    private Date validTillDate;

    public Integer getDirectDebitScheme() {
        return directDebitScheme;
    }

    public void setDirectDebitScheme(Integer directDebitScheme) {
        this.directDebitScheme = directDebitScheme;
    }

    public Integer getDirectDebitType() {
        return directDebitType;
    }

    public void setDirectDebitType(Integer directDebitType) {
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

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Date getValidTillDate() {
        return validTillDate;
    }

    public void setValidTillDate(Date validTillDate) {
        this.validTillDate = validTillDate;
    }
}

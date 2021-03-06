package com.optimus.eds.db.entities.pricing;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {   @ForeignKey(entity = PriceConditionType.class
                ,parentColumns = "priceConditionTypeId",
                childColumns = "priceConditionTypeId",
                onDelete=ForeignKey.CASCADE),

        @ForeignKey(entity = PriceAccessSequence.class
                ,parentColumns = "priceAccessSequenceId",
                childColumns = "accessSequenceId",onDelete=ForeignKey.CASCADE)},
        indices = {@Index("accessSequenceId"),@Index( "priceConditionTypeId")})
public class PriceCondition {

    @PrimaryKey
    private int priceConditionId;
    private String name;

    private Boolean isBundle;
    private Integer pricingType;
    private String validFrom;
    private String validTo;


    @Ignore
    private List<PriceConditionEntities> priceConditionEntities;

    @Ignore
    private List<PriceConditionDetail> priceConditionDetails;


    public int getPriceConditionId() {
        return priceConditionId;
    }

    public void setPriceConditionId(int priceConditionId) {
        this.priceConditionId = priceConditionId;
    }

    public int getPriceConditionTypeId() {
        return priceConditionTypeId;
    }

    public void setPriceConditionTypeId(int conditionTypeId) {
        this.priceConditionTypeId = conditionTypeId;
    }

    public int getAccessSequenceId() {
        return accessSequenceId;
    }

    public void setAccessSequenceId(int accessSequenceId) {
        this.accessSequenceId = accessSequenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getBundle() {
        return isBundle;
    }

    public void setBundle(Boolean bundle) {
        isBundle = bundle;
    }

    public Integer getPricingType() {
        return pricingType;
    }

    public void setPricingType(Integer pricingType) {
        this.pricingType = pricingType;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public List<PriceConditionEntities> getPriceConditionEntities() {
        return priceConditionEntities;
    }

    public void setPriceConditionEntities(List<PriceConditionEntities> priceConditionEntities) {
        this.priceConditionEntities = priceConditionEntities;
    }

    public List<PriceConditionDetail> getPriceConditionDetails() {
        return priceConditionDetails;
    }

    public void setPriceConditionDetails(List<PriceConditionDetail> priceConditionDetails) {
        this.priceConditionDetails = priceConditionDetails;
    }

    /*ForeignKeys*/

 /*   @ForeignKey(entity = PriceConditionType.class
            ,parentColumns = "priceConditionTypeId",
            childColumns = "priceConditionTypeId",
            onDelete=ForeignKey.CASCADE)*/
    private int priceConditionTypeId;


/*    @ForeignKey(entity = PriceAccessSequence.class
            ,parentColumns = "priceAccessSequenceId",
            childColumns = "accessSequenceId",onDelete=ForeignKey.CASCADE)*/
    private int accessSequenceId;

}

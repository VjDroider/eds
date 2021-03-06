package com.optimus.eds.db.entities.pricing;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys =
                {
                        @ForeignKey(
                                entity = PriceConditionClass.class,
                                parentColumns = "priceConditionClassId",
                                childColumns = "priceConditionClassId",
                                onUpdate = ForeignKey.CASCADE,
                                onDelete = ForeignKey.CASCADE)

                },
        indices = { @Index(value = {"priceScaleBasisId","priceConditionTypeId"}), @Index("priceConditionClassId")})
public class PriceConditionType {

    @PrimaryKey
    private int priceConditionTypeId;
    private String name;

    private int priceConditionClassId;
    @NonNull
    private int operationType;
    @NonNull
    private int calculationType;
    @NonNull
    private int roundingRule;


    private Integer priceScaleBasisId;


    @Ignore
    private List<PriceCondition> priceConditions;

    public int getPriceConditionTypeId() {
        return priceConditionTypeId;
    }

    public void setPriceConditionTypeId(int priceConditionTypeId) {
        this.priceConditionTypeId = priceConditionTypeId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceConditionClassId() {
        return priceConditionClassId;
    }

    public void setPriceConditionClassId(int conditionClassId) {
        this.priceConditionClassId = conditionClassId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(int calculationType) {
        this.calculationType = calculationType;
    }

    public int getRoundingRule() {
        return roundingRule;
    }

    public void setRoundingRule(int roundingRule) {
        this.roundingRule = roundingRule;
    }

    public Integer getPriceScaleBasisId() {
        return priceScaleBasisId;
    }

    public void setPriceScaleBasisId(Integer priceScaleBasisId) {
        this.priceScaleBasisId = priceScaleBasisId;
    }
    public List<PriceCondition> getPriceConditions() {
        return priceConditions;
    }

    public void setPriceConditions(List<PriceCondition> priceConditions) {
        this.priceConditions = priceConditions;
    }

}

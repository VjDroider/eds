package com.optimus.eds.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Product implements Serializable {


    @PrimaryKey @ColumnInfo(name = "pid")
    @SerializedName("productId")
    public Long id;
    @SerializedName("productName")
    public String name;
    @SerializedName("productDescription")
    public String productDescription;
    @SerializedName("productCode")
    public String productCode;
    @SerializedName("productGroupId")
    public Long productGroupId;
    @SerializedName("productGroupName")
    public String productGroupName;
    @SerializedName("productPackageId")
    public Long pkgId;
    @SerializedName("packageName")
    public String packageName;
    @SerializedName("productBrandId")
    public Long productBrandId;
    @SerializedName("brandName")
    public String brandName;
    @SerializedName("productFlavorId")
    public Long productFlavorId;
    @SerializedName("flavorName")
    public String flavorName;
    @SerializedName("unitCode")
    public String unitCode;
    @SerializedName("cartonCode")
    public String cartonCode;
    @SerializedName("unitQuantity")
    public Long unitQuantity;
    @SerializedName("cartonQuantity")
    public Integer cartonQuantity;
    @SerializedName("unitSizeForDisplay")
    public String unitSizeForDisplay;
    @SerializedName("cartonSizeForDisplay")
    public String cartonSizeForDisplay;
    @SerializedName("unitStockInHand")
    public Long unitStockInHand;
    @SerializedName("cartonStockInHand")
    public Long cartonStockInHand;


    @Ignore
    public Long qtyCarton=0l;
    @Ignore
    public Long qtyUnit=0l;


    public Product(Long id, Long pkgId, String name) {
        this.id = id;
        this.pkgId = pkgId;
        this.name = name;
    }

    public Product(Long id, Long pkgId, String name, Long qtyUnit) {
        this.id = id;
        this.pkgId = pkgId;
        this.name = name;
        this.qtyUnit = qtyUnit;
    }
    public Long getQtyUnit() {
        return qtyUnit==null||qtyUnit==0?null:qtyUnit;
    }

    public Long getQtyCarton() {
        return qtyCarton==null||qtyCarton==0?null:qtyCarton;
    }

    public void setQty(Long carton,Long unit) {
        this.qtyCarton = carton;
        this.qtyUnit = unit;
    }

    public void setUnit(Long unit) {
        this.qtyUnit = unit;
    }

    public void setCarton(Long carton) {
        this.qtyCarton = carton;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProductDescription() {
        return productDescription;
    }
    public Long getPkgId() {
        return pkgId;
    }

    public String getProductCode() {
        return productCode;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Long getProductBrandId() {
        return productBrandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public Long getProductFlavorId() {
        return productFlavorId;
    }

    public String getFlavorName() {
        return flavorName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getCartonCode() {
        return cartonCode;
    }

    public Long getUnitQuantity() {
        return unitQuantity;
    }

    public Integer getCartonQuantity() {
        return cartonQuantity;
    }

    public String getUnitSizeForDisplay() {
        return unitSizeForDisplay;
    }

    public String getCartonSizeForDisplay() {
        return cartonSizeForDisplay;
    }

    public Long getUnitStockInHand() {
        return unitStockInHand;
    }

    public Long getCartonStockInHand() {
        return cartonStockInHand;
    }

    public boolean isProductSelected(){
        return (getQtyCarton()!=null || getQtyUnit()!=null &&( getQtyUnit()>0 || getQtyCarton()>0));
    }

    @Override
    public String toString() {
        return String.valueOf(getQtyCarton());
    }
}

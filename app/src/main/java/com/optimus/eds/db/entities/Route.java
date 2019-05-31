
package com.optimus.eds.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

@Entity(tableName = "Route")
public class Route implements Serializable {

    @SerializedName("routeId")
    @PrimaryKey
    public Long mRouteId;

    @SerializedName("routeName")
    public String mRouteName;

    @SerializedName("employeeId")
    public Integer mEmployeeId;

    @SerializedName("totalOutlets")
    public Integer mTotalOutlets;


    /*******************Setters*********************/
    public void setRouteId(Long mRouteId) {
        this.mRouteId = mRouteId;
    }

    public void setRouteName(String mRouteName) {
        this.mRouteName = mRouteName;
    }
        public void setEmployeeId(Integer employeeId){
        this.mEmployeeId = employeeId;
    }

    /******************Getters**********************/

    public Integer getTotalOutlets() {
        return mTotalOutlets;
    }

    public Long getRouteId() {
        return mRouteId;
    }

    public String getRouteName() {
        return mRouteName;
    }

    public Integer getEmployeeId() {
        return mEmployeeId;
    }



    @Override
    public String toString() {
        return this.getRouteName();            // What to display in the Spinner list.
    }

}

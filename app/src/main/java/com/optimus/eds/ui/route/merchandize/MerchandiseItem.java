package com.optimus.eds.ui.route.merchandize;

import com.optimus.eds.Constant;

import java.io.Serializable;

/**
 * Created By apple on 4/23/19
 */


public class MerchandiseItem implements Serializable {

    int id;
    String path;

    int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

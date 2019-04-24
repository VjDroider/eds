package com.optimus.eds.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimus.eds.db.entities.Merchandise;
import com.optimus.eds.ui.route.merchandize.MerchandiseItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By apple on 4/24/19
 */

public class MerchandiseItemConverter {

    @TypeConverter
    public static List<MerchandiseItem> fromString(String value) {
        if(value==null)
            return (null);
        Type listType = new TypeToken<ArrayList<MerchandiseItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MerchandiseItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

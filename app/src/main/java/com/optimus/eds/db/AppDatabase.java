package com.optimus.eds.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.optimus.eds.db.converters.AssetConverter;
import com.optimus.eds.db.converters.MerchandiseItemConverter;
import com.optimus.eds.db.converters.OutletConverter;
import com.optimus.eds.db.converters.ProductConverter;
import com.optimus.eds.db.dao.CustomerDao;
import com.optimus.eds.db.dao.MerchandiseDao;
import com.optimus.eds.db.dao.OrderDao;
import com.optimus.eds.db.dao.ProductsDao;
import com.optimus.eds.db.dao.RouteDao;
import com.optimus.eds.db.entities.Asset;
import com.optimus.eds.db.entities.CartonPriceBreakDown;
import com.optimus.eds.db.entities.CustomerInput;
import com.optimus.eds.db.entities.Merchandise;
import com.optimus.eds.db.entities.Order;
import com.optimus.eds.db.entities.OrderDetail;
import com.optimus.eds.db.entities.Outlet;
import com.optimus.eds.db.entities.Package;
import com.optimus.eds.db.entities.Product;
import com.optimus.eds.db.entities.ProductGroup;
import com.optimus.eds.db.entities.Route;
import com.optimus.eds.db.entities.UnitPriceBreakDown;


@Database(entities = {Route.class, Outlet.class, Merchandise.class, Asset.class,ProductGroup.class,
        Product.class, Package.class, Order.class,
        OrderDetail.class, CartonPriceBreakDown.class,
        UnitPriceBreakDown.class, CustomerInput.class
}, version = 1, exportSchema = false)
@TypeConverters({OutletConverter.class, MerchandiseItemConverter.class, AssetConverter.class, ProductConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract RouteDao routeDao();
    public abstract ProductsDao productsDao();
    public abstract OrderDao orderDao();
    public abstract MerchandiseDao merchandiseDao();
    public abstract CustomerDao customerDao();
    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "eds")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static AppDatabase getMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

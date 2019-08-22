package com.optimus.eds.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.optimus.eds.db.entities.Asset;
import com.optimus.eds.db.entities.Outlet;
import com.optimus.eds.db.entities.Route;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface RouteDao extends MerchandiseDao{

    @Query("SELECT * FROM Route ORDER BY mRouteName ASC")
    LiveData<List<Route>> findAllRoutes();

    @Query("SELECT * FROM Route")
    Single<List<Route>> getAllRoutes();

    @Query("SELECT * FROM Route WHERE mRouteId=:id")
    LiveData<Route> findRouteById(Long id);

    @Query("SELECT * FROM Outlet ORDER BY mOutletName ASC")
    LiveData<List<Outlet>> findAllOutlets();

    @Query("SELECT * FROM Outlet WHERE mRouteId=:routeId AND planned=:planned")
    Maybe<List<Outlet>> findAllOutletsForRoute(Long routeId,int planned);

    @Query("SELECT * FROM Outlet WHERE mOutletId=:id")
    LiveData<Outlet> findOutletById(Long id);


    @Insert(onConflict = REPLACE)
    long insertRoute(Route route);

    @Insert(onConflict = REPLACE)
    void insertRoutes(List<Route> routes);

    @Update
    int updateRoute(Route route);

    @Update
    void updateRoute(List<Route> routes);

    @Delete
    void deleteRoute(Route route);

    @Insert(onConflict = REPLACE)
    long insertOutlet(Outlet outlet);

    @Insert(onConflict = REPLACE)
    void insertOutlets(List<Outlet> outlets);

    @Insert(onConflict = REPLACE)
    void insertAssets(List<Asset> assets);

    @Update
    int updateOutlet(Outlet outlet);

    @Update
    void updateOutlet(List<Outlet> outlets);

    @Delete
    void deleteOutlet(Outlet outlet);

    @Query("DELETE FROM Outlet")
    void deleteAllOutlets();

    @Query("DELETE FROM Route")
    void deleteAllRoutes();


}

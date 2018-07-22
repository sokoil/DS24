package ru.weblokos.ds24.DB.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.weblokos.ds24.DB.Entity.QuoteEntity;

@Dao
public interface QuoteDao {

    @Query("select * from quotes")
    LiveData<List<QuoteEntity>> loadAllQuotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuoteEntity> quotes);

    @Query("delete from quotes")
    void deleteQuotes();
}

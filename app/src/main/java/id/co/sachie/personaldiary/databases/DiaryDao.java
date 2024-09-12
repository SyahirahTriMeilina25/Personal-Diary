package id.co.sachie.personaldiary.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.co.sachie.personaldiary.models.Diary;

@Dao
public interface DiaryDao {
    @Insert
    long[] insertDiaries(Diary...diaries);
    @Delete
    int delete (Diary...diaries);
    @Update
    int update(Diary...diaries);

    @Query("SELECT * FROM diaries")
    LiveData<List<Diary>> getDiaries();

    @Query("SELECT * FROM diaries WHERE title LIKE :title")
    List<Diary> getDiaryWithCustomQuery(String title);
}

package id.co.sachie.personaldiary.databases;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.co.sachie.personaldiary.async.DeleteAsyncTask;
import id.co.sachie.personaldiary.async.InsertAsyncTask;
import id.co.sachie.personaldiary.models.Diary;

public class DiaryRepository {
    private DiaryDatabase diaryDatabase;

    public DiaryRepository (Context context){
        diaryDatabase = DiaryDatabase.getInstance(context);
    }
    public void insertDiaryTask(Diary diary){
        new InsertAsyncTask(diaryDatabase.getDiaryDao()).execute(diary);
    }

    public void updateDiary(Diary diary){
    }

    public LiveData<List<Diary>> retrieveDiaryTask(){
        return diaryDatabase.getDiaryDao().getDiaries();
    }

    public void deleteDiary(Diary diary){
        new DeleteAsyncTask(diaryDatabase.getDiaryDao()).execute(diary);
    }
}

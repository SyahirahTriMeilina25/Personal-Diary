package id.co.sachie.personaldiary.async;

import android.os.AsyncTask;

import id.co.sachie.personaldiary.databases.DiaryDao;
import id.co.sachie.personaldiary.models.Diary;

public class InsertAsyncTask extends AsyncTask<Diary, Void, Void> {
    private DiaryDao mDiaryDao;

    public InsertAsyncTask(DiaryDao diaryDao){
        mDiaryDao = diaryDao;
    }

    @Override
    protected Void doInBackground(Diary... diaries) {
        mDiaryDao.insertDiaries(diaries);
        return null;
    }
}

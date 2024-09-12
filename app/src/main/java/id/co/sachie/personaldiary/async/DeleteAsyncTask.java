package id.co.sachie.personaldiary.async;

import android.os.AsyncTask;

import id.co.sachie.personaldiary.databases.DiaryDao;
import id.co.sachie.personaldiary.models.Diary;

public class DeleteAsyncTask extends AsyncTask<Diary, Void, Void> {
    private static final String TAG = "DeleteAsyncTask";
    private DiaryDao mDiaryDao;
    public DeleteAsyncTask(DiaryDao dao){
        mDiaryDao = dao;
    }
    @Override
    protected Void doInBackground(Diary... diaries) {
        return null;
    }
}

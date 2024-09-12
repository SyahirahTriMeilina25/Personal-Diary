package id.co.sachie.personaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import id.co.sachie.personaldiary.adapters.DiaryRecyclerViewAdapter;
import id.co.sachie.personaldiary.databases.DiaryRepository;
import id.co.sachie.personaldiary.models.Diary;

public class MainActivity extends AppCompatActivity implements DiaryRecyclerViewAdapter.onDiaryListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Diary> listDiary = new ArrayList<>();
    private DiaryRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private DiaryRepository diaryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.diary_rv);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        diaryRepository = new DiaryRepository(this);

        initRecyclerView();
        //insertDummyData();
        retrievesDiaryData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("My Personal Diary");
        setSupportActionBar(toolbar);
    }
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    deleteDiary(listDiary.get(viewHolder.getAdapterPosition()));

                }
            };
            private void deleteDiary(Diary diary) {
                listDiary.remove(diary);
                adapter.notifyDataSetChanged();

                diaryRepository.deleteDiary(diary);
            }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DiaryRecyclerViewAdapter(listDiary, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }
    private void insertDummyData(){
        for (int i = 1; i < 100; i++) {
            Diary diary = new Diary();
            diary.setTitle("Diary ke-" + i);
            diary.setTimestamp("JUNI 2023");
            listDiary.add(diary);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDiaryClick(int position) {
        Toast.makeText(this, "Anda Memilih diary ke-"+(position+1), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, DiaryDetailsActivity.class);
        intent.putExtra("diary", listDiary.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, DiaryDetailsActivity.class);
        startActivity(intent);
    }

    private void retrievesDiaryData(){
        diaryRepository.retrieveDiaryTask().observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> diaries) {
                if (listDiary.size()>0){
                    listDiary.clear();
                }
                if (diaries!=null){
                    listDiary.addAll(diaries);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
package id.co.sachie.personaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.co.sachie.personaldiary.databases.DiaryRepository;
import id.co.sachie.personaldiary.models.Diary;
import id.co.sachie.personaldiary.utils.DateUtil;

public class DiaryDetailsActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnClickListener, TextWatcher {
    private static final String TAG = "DiaryDetailsActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    private int page;
    private Diary diary;
    private EditText et_diary_content;
    private EditText et_toolbar_edit;
    private TextView text_toolbar_view;
    private RelativeLayout rl_back, rl_check;
    private ImageButton ib_check, ib_back;
    private TextView titleView;
    private EditText editView;
    private int diaryMode;

    private boolean isNewDiary;
    private GestureDetector gestureDetector;
    private DiaryRepository diaryRepository;
    private Diary finalDiary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        diaryRepository = new DiaryRepository(this);

        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        et_diary_content = findViewById(R.id.contentDiary);
        et_toolbar_edit = findViewById(R.id.title_edit);
        text_toolbar_view = findViewById(R.id.title_view);

        rl_back = findViewById(R.id.back_button);
        rl_check = findViewById(R.id.check_button);
        ib_back = findViewById(R.id.toolbar_back_button);
        ib_check = findViewById(R.id.toolbar_check_button);
        titleView = findViewById(R.id.title_view);
        editView = findViewById(R.id.title_edit);



        if (getIncomingIntent()){
            //diary baru (Edit mode)
            setNewDiaryProperties();
            enableEditMode();
        }else{
            //sy
            setDiaryProperties();
            disableEditMode();
        }
        setListener();

    }
    private void saveChanges(){
        if (isNewDiary){
            saveNewDiary();
        }
    }

    private void saveNewDiary() {
        diaryRepository.insertDiaryTask(finalDiary);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view==null){
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    private void enableEditMode(){
        rl_back.setVisibility(View.GONE);
        rl_check.setVisibility(View.VISIBLE);

        titleView.setVisibility(View.GONE);
        editView.setVisibility(View.VISIBLE);

        diaryMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
    }

    private void disableEditMode(){
        rl_back.setVisibility(View.VISIBLE);
        rl_check.setVisibility(View.GONE);

        titleView.setVisibility(View.VISIBLE);
        editView.setVisibility(View.GONE);

        diaryMode = EDIT_MODE_DISABLED;
        disableContentInteraction();

        String temp = et_diary_content.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if(temp.length()>0){
            finalDiary.setTitle(editView.getText().toString());
            finalDiary.setContent(et_diary_content.getText().toString());
            String timestamp = DateUtil.getCurrentTimestamp();
            finalDiary .setTimestamp(timestamp);

            if(!finalDiary.getContent().equals(diary.getContent()) || !finalDiary.getTitle().equals(diary.getTitle())){
                saveChanges();
            }
        }
    }


    private void setListener() {
        et_diary_content.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, this);
        ib_check.setOnClickListener(this);
        titleView.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        editView.addTextChangedListener(this);

    }

    private void setDiaryProperties(){
        et_toolbar_edit.setText(diary.getTitle());
        text_toolbar_view.setText(diary.getTitle());
        et_diary_content.setText(diary.getContent());
    }
    private void setNewDiaryProperties(){
        et_toolbar_edit.setText("New Diary");
        text_toolbar_view.setText("New Diary");
        diary = new Diary();
        finalDiary = new Diary();
        diary.setTitle("Diary Title");
        finalDiary.setTitle("Diary Title");
    }



    private boolean getIncomingIntent(){
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("diary")){
            diary = intent.getParcelableExtra("diary");
            finalDiary = getIntent().getParcelableExtra("diary");
            diaryMode = EDIT_MODE_DISABLED;
            return false;
        }
        diaryMode = EDIT_MODE_ENABLED;
        isNewDiary = true;
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState,
                                    @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("mode", diaryMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        diaryMode = savedInstanceState.getInt("mode");
        if (diaryMode==EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    private void disableContentInteraction(){
        et_diary_content.setFocusable(false);
        et_diary_content.setFocusableInTouchMode(false);
        et_diary_content.setClickable(false);
        et_diary_content.setCursorVisible(false);
        et_diary_content.setLongClickable(false);
        et_diary_content.setTextIsSelectable(false);
        et_diary_content.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        hideVirtualKeyboard();
    }

    private void enableContentInteraction(){
        et_diary_content.setFocusable(true);
        et_diary_content.setFocusableInTouchMode(true);
        et_diary_content.setClickable(true);
        et_diary_content.setCursorVisible(true);
        et_diary_content.setLongClickable(true);
        et_diary_content.setTextIsSelectable(true);
        et_diary_content.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et_diary_content.requestFocus();
    }

    @Override
    public void onBackPressed(){
        if (diaryMode==EDIT_MODE_ENABLED){
            onClick(ib_check);
        }else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        enableEditMode();
        Toast.makeText(this, "Double Click Terdeteksi", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.toolbar_check_button:
//                disableEditMode();
//                hideVirtualKeyboard();
//                break;
//            case R.id.title_view:
//                enableEditMode();
//                et_toolbar_edit.requestFocus();
//                et_toolbar_edit.setSelection(et_toolbar_edit.length());
//                break;
//            case  R.id.toolbar_back_button:
//                finish();
//                break;
//        }

        int id = view.getId();

        if (id == R.id.toolbar_check_button){
            hideVirtualKeyboard();
            disableEditMode();
        } else if (id == R.id.title_view) {
            enableEditMode();
            et_toolbar_edit.requestFocus();
            et_toolbar_edit.setSelection(et_toolbar_edit.length());
        } else if (id == R.id.toolbar_back_button) {
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        titleView.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
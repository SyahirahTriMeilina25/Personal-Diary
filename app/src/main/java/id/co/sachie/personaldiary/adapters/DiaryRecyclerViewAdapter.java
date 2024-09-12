package id.co.sachie.personaldiary.adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.sachie.personaldiary.R;
import id.co.sachie.personaldiary.models.Diary;
import id.co.sachie.personaldiary.utils.DateUtil;

public class DiaryRecyclerViewAdapter extends RecyclerView.Adapter<DiaryRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Diary> mDiary = new ArrayList<>();
    private onDiaryListener mOnDiaryListener;
    public DiaryRecyclerViewAdapter(ArrayList<Diary> mDiary, onDiaryListener onDiaryListener){
        this.mDiary = mDiary;
        this.mOnDiaryListener = onDiaryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_diary, parent, false);
        return new ViewHolder(view, mOnDiaryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String month = mDiary.get(position).getTimestamp().substring(0,2);
            month = DateUtil.getMonthFromNumber(month);
            String year = mDiary.get(position).getTimestamp().substring(3);
            String timestamp = month+ " " + year;
            holder.title.setText(mDiary.get(position).getTitle());
            holder.timestamp.setText(timestamp);
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDiary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView timestamp;
        onDiaryListener onDiaryListener;

        public ViewHolder(@NonNull View itemView, DiaryRecyclerViewAdapter.onDiaryListener onDiaryListener) {
            super(itemView);
            title = itemView.findViewById(R.id.diary_title);
            timestamp = itemView.findViewById(R.id.diary_timestamp);
            this.onDiaryListener = onDiaryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDiaryListener.onDiaryClick(getAdapterPosition());
        }
    }
    public interface onDiaryListener{
        void onDiaryClick(int position);;
    }
}

package com.mindspace.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.utils.DateUtils;

public class MoodAdapter extends ListAdapter<MoodRecord, MoodAdapter.MoodViewHolder> {
    
    private OnMoodClickListener listener;

    public MoodAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<MoodRecord> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<MoodRecord>() {
            @Override
            public boolean areItemsTheSame(@NonNull MoodRecord oldItem, @NonNull MoodRecord newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull MoodRecord oldItem, @NonNull MoodRecord newItem) {
                return oldItem.getMoodType().equals(newItem.getMoodType())
                    && oldItem.getMoodScore() == newItem.getMoodScore()
                    && oldItem.getCreatedAt() == newItem.getCreatedAt();
            }
        };

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodRecord mood = getItem(position);
        holder.bind(mood);
    }

    public void setOnMoodClickListener(OnMoodClickListener listener) {
        this.listener = listener;
    }

    public interface OnMoodClickListener {
        void onMoodClick(MoodRecord mood);
        void onMoodLongClick(MoodRecord mood);
    }

    class MoodViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView ivMoodIcon;
        private final View moodBg;
        private final TextView tvMoodType;
        private final TextView tvMoodContent;
        private final TextView tvMoodTime;
        private final TextView tvMoodScore;

        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoodIcon = itemView.findViewById(R.id.iv_mood_icon);
            moodBg = itemView.findViewById(R.id.mood_bg);
            tvMoodType = itemView.findViewById(R.id.tv_mood_type);
            tvMoodContent = itemView.findViewById(R.id.tv_mood_content);
            tvMoodTime = itemView.findViewById(R.id.tv_mood_time);
            tvMoodScore = itemView.findViewById(R.id.tv_mood_score);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMoodClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMoodLongClick(getItem(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(MoodRecord mood) {
            tvMoodType.setText(getMoodTypeDisplayName(mood.getMoodType()));
            tvMoodContent.setText(mood.getContent());
            tvMoodTime.setText(DateUtils.getRelativeTimeString(mood.getCreatedAt()));
            
            int iconRes = getMoodIconResource(mood.getMoodType());
            ivMoodIcon.setImageResource(iconRes);
            
            tvMoodScore.setText("☀️ " + (int)mood.getMoodScore());
        }

        private String getMoodTypeDisplayName(String moodType) {
            switch (moodType) {
                case MoodRecord.MoodType.HAPPY: return "开心";
                case MoodRecord.MoodType.EXCITED: return "兴奋";
                case MoodRecord.MoodType.NORMAL: return "平静";
                case MoodRecord.MoodType.SAD: return "难过";
                case MoodRecord.MoodType.ANGRY: return "生气";
                default: return moodType;
            }
        }

        private int getMoodIconResource(String moodType) {
            switch (moodType) {
                case MoodRecord.MoodType.HAPPY: return R.drawable.ic_mood_happy;
                case MoodRecord.MoodType.EXCITED: return R.drawable.ic_mood_excited;
                case MoodRecord.MoodType.NORMAL: return R.drawable.ic_mood_normal;
                case MoodRecord.MoodType.SAD: return R.drawable.ic_mood_sad;
                case MoodRecord.MoodType.ANGRY: return R.drawable.ic_mood_angry;
                default: return R.drawable.ic_mood_normal;
            }
        }
    }
}

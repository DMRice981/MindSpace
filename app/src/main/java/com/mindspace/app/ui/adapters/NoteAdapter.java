package com.mindspace.app.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.data.model.Note;
import com.mindspace.app.utils.DateUtils;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    
    private OnNoteClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getContent().equals(newItem.getContent())
                    && oldItem.getUpdatedAt() == newItem.getUpdatedAt();
            }
        };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        
        private final View colorIndicator;
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvCategory;
        private final TextView tvTime;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvTime = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNoteClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNoteLongClick(getItem(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Note note) {
            tvTitle.setText(note.getTitle());
            tvContent.setText(note.getContent());
            tvCategory.setText(getCategoryDisplayName(note.getCategory()));
            tvTime.setText(DateUtils.getRelativeTimeString(note.getUpdatedAt()));
            
            try {
                colorIndicator.setBackgroundColor(Color.parseColor(getCategoryColor(note.getCategory())));
            } catch (Exception e) {
                colorIndicator.setBackgroundColor(Color.parseColor("#7C4DFF"));
            }
        }

        private String getCategoryDisplayName(String category) {
            switch (category) {
                case Note.Category.WORK: return "工作";
                case Note.Category.STUDY: return "学习";
                case Note.Category.LIFE: return "生活";
                case Note.Category.IDEA: return "创意";
                default: return category;
            }
        }

        private String getCategoryColor(String category) {
            switch (category) {
                case Note.Category.WORK: return "#FF6D9A";
                case Note.Category.STUDY: return "#7C4DFF";
                case Note.Category.LIFE: return "#00C853";
                case Note.Category.IDEA: return "#FFAB00";
                default: return "#7C4DFF";
            }
        }
    }
}

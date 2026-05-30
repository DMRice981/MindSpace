package com.mindspace.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.model.Quote;
import com.mindspace.app.ui.adapters.MoodAdapter;
import com.mindspace.app.utils.QuoteFetcher;
import com.mindspace.app.viewmodel.MoodViewModel;

public class HomeFragment extends Fragment {
    
    private MoodViewModel moodViewModel;
    private MoodAdapter adapter;
    
    private RecyclerView recyclerView;
    private EditText etMoodContent;
    private ImageButton btnHappy, btnExcited, btnNormal, btnSad, btnAngry;
    private Button btnSaveMood;
    private TextView tvQuoteContent, tvQuoteAuthor;
    private ImageButton btnRefreshQuote;
    private LinearLayout layoutEmpty;
    
    private String selectedMoodType = null;
    private Toast currentToast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupMoodButtons();
        setupViewModel();
        loadDailyQuote();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_recent_moods);
        etMoodContent = view.findViewById(R.id.et_mood_content);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        
        btnHappy = view.findViewById(R.id.btn_happy);
        btnExcited = view.findViewById(R.id.btn_excited);
        btnNormal = view.findViewById(R.id.btn_normal);
        btnSad = view.findViewById(R.id.btn_sad);
        btnAngry = view.findViewById(R.id.btn_angry);
        btnSaveMood = view.findViewById(R.id.btn_save_mood);
        
        tvQuoteContent = view.findViewById(R.id.tv_quote_content);
        tvQuoteAuthor = view.findViewById(R.id.tv_quote_author);
        btnRefreshQuote = view.findViewById(R.id.btn_refresh_quote);
    }

    private void loadDailyQuote() {
        if (getContext() == null) {
            return;
        }
        
        Quote savedQuote = QuoteFetcher.getSavedQuote(requireContext());
        displayQuote(savedQuote);
        
        QuoteFetcher.getDailyQuote(requireContext(), new QuoteFetcher.QuoteListener() {
            @Override
            public void onQuoteLoaded(Quote quote) {
                if (isAdded() && getContext() != null) {
                    displayQuote(quote);
                }
            }

            @Override
            public void onQuoteError(String error) {
                // 静默处理错误，不显示Toast避免空指针
            }
        });

        btnRefreshQuote.setOnClickListener(v -> {
            if (getContext() == null) {
                return;
            }
            QuoteFetcher.fetchNewQuote(requireContext(), new QuoteFetcher.QuoteListener() {
                @Override
                public void onQuoteLoaded(Quote quote) {
                    if (isAdded() && getContext() != null) {
                        displayQuote(quote);
                    }
                }

                @Override
                public void onQuoteError(String error) {
                    if (isAdded() && getContext() != null) {
                        showToast(R.string.toast_quote_load_failed);
                    }
                }
            });
        });
    }

    private void displayQuote(Quote quote) {
        if (tvQuoteContent != null && tvQuoteAuthor != null) {
            tvQuoteContent.setText("\"" + quote.getContent() + "\"");
            tvQuoteAuthor.setText("—— " + quote.getAuthor());
        }
    }

    private void setupRecyclerView() {
        adapter = new MoodAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnMoodClickListener(new MoodAdapter.OnMoodClickListener() {
            @Override
            public void onMoodClick(MoodRecord mood) {
                // 移除点击记录的 Toast，避免累积
            }

            @Override
            public void onMoodLongClick(MoodRecord mood) {
                showDeleteDialog(mood);
            }
        });
    }

    private void setupMoodButtons() {
        btnHappy.setOnClickListener(v -> selectMoodType(MoodRecord.MoodType.HAPPY));
        btnExcited.setOnClickListener(v -> selectMoodType(MoodRecord.MoodType.EXCITED));
        btnNormal.setOnClickListener(v -> selectMoodType(MoodRecord.MoodType.NORMAL));
        btnSad.setOnClickListener(v -> selectMoodType(MoodRecord.MoodType.SAD));
        btnAngry.setOnClickListener(v -> selectMoodType(MoodRecord.MoodType.ANGRY));
        
        btnSaveMood.setOnClickListener(v -> saveMood());
    }

    private void selectMoodType(String moodType) {
        selectedMoodType = moodType;
        if (moodViewModel != null) {
            moodViewModel.setSelectedMoodType(moodType);
        }
        
        updateMoodButtonStates();
        
        // 移除选择心情时的 Toast，避免快速点击累积
    }
    
    private void updateMoodButtonStates() {
        if (btnHappy != null) {
            btnHappy.setSelected(selectedMoodType != null && selectedMoodType.equals(MoodRecord.MoodType.HAPPY));
        }
        if (btnExcited != null) {
            btnExcited.setSelected(selectedMoodType != null && selectedMoodType.equals(MoodRecord.MoodType.EXCITED));
        }
        if (btnNormal != null) {
            btnNormal.setSelected(selectedMoodType != null && selectedMoodType.equals(MoodRecord.MoodType.NORMAL));
        }
        if (btnSad != null) {
            btnSad.setSelected(selectedMoodType != null && selectedMoodType.equals(MoodRecord.MoodType.SAD));
        }
        if (btnAngry != null) {
            btnAngry.setSelected(selectedMoodType != null && selectedMoodType.equals(MoodRecord.MoodType.ANGRY));
        }
    }

    private void saveMood() {
        if (selectedMoodType == null) {
            showToast(R.string.toast_error_select_mood);
            return;
        }
        
        if (etMoodContent == null) {
            return;
        }
        
        String content = etMoodContent.getText().toString().trim();
        if (content.isEmpty()) {
            showToast(R.string.toast_error_empty_content);
            return;
        }
        
        if (moodViewModel != null) {
            moodViewModel.saveMood(selectedMoodType, content);
        }
        
        etMoodContent.setText("");
        selectedMoodType = null;
        updateMoodButtonStates();
        showToast(R.string.toast_mood_saved);
    }

    private void setupViewModel() {
        moodViewModel = new ViewModelProvider(this).get(MoodViewModel.class);
        
        moodViewModel.getRecentMoods(10).observe(getViewLifecycleOwner(), moods -> {
            if (adapter != null) {
                adapter.submitList(moods);
            }
            updateEmptyState(moods == null || moods.isEmpty());
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        if (recyclerView != null) {
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }

    private void showDeleteDialog(MoodRecord mood) {
        if (getContext() == null) {
            return;
        }
        
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_delete_confirm_title)
            .setMessage(R.string.dialog_delete_confirm_message)
            .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                if (moodViewModel != null) {
                    moodViewModel.delete(mood);
                }
                showToast(R.string.toast_deleted);
            })
            .setNegativeButton(R.string.btn_cancel, null)
            .show();
    }

    private String getMoodDisplayName(String moodType) {
        if (moodType == null) {
            return "";
        }
        
        switch (moodType) {
            case MoodRecord.MoodType.HAPPY: return getString(R.string.mood_happy);
            case MoodRecord.MoodType.EXCITED: return getString(R.string.mood_excited);
            case MoodRecord.MoodType.NORMAL: return getString(R.string.mood_normal);
            case MoodRecord.MoodType.SAD: return getString(R.string.mood_sad);
            case MoodRecord.MoodType.ANGRY: return getString(R.string.mood_angry);
            default: return moodType;
        }
    }
    
    private void showToast(int resId) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        if (getContext() != null) {
            currentToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
            currentToast.show();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentToast != null) {
            currentToast.cancel();
        }
        recyclerView = null;
        etMoodContent = null;
        btnHappy = null;
        btnExcited = null;
        btnNormal = null;
        btnSad = null;
        btnAngry = null;
        btnSaveMood = null;
        tvQuoteContent = null;
        tvQuoteAuthor = null;
        btnRefreshQuote = null;
        layoutEmpty = null;
        adapter = null;
    }
}

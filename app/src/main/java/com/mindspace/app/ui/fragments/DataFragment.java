package com.mindspace.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.example.mindspace.R;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.utils.ChartUtils;
import com.mindspace.app.utils.DateUtils;
import com.mindspace.app.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFragment extends Fragment {
    
    private DataViewModel dataViewModel;
    
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart barChartWeekly;
    private TextView tvAverageScore;
    private TextView tvMoodDesc;
    private TextView tvHappyCount;
    private TextView tvExcitedCount;
    private TextView tvCalmCount;
    private TextView tvTotalRecords;
    private TextView tvBestMood;
    private TextView tvStreak;
    
    private List<MoodRecord> recentMoods = new ArrayList<>();
    private int totalRecords = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupCharts();
        setupViewModel();
    }

    private void initViews(View view) {
        lineChart = view.findViewById(R.id.line_chart);
        pieChart = view.findViewById(R.id.pie_chart);
        barChartWeekly = view.findViewById(R.id.bar_chart_weekly);
        tvAverageScore = view.findViewById(R.id.tv_average_score);
        tvMoodDesc = view.findViewById(R.id.tv_mood_desc);
        tvHappyCount = view.findViewById(R.id.tv_happy_count);
        tvExcitedCount = view.findViewById(R.id.tv_excited_count);
        tvCalmCount = view.findViewById(R.id.tv_calm_count);
        tvTotalRecords = view.findViewById(R.id.tv_total_records);
        tvBestMood = view.findViewById(R.id.tv_best_mood);
        tvStreak = view.findViewById(R.id.tv_streak);
    }

    private void setupCharts() {
        ChartUtils.setupLineChart(lineChart);
        ChartUtils.setupPieChart(pieChart);
        ChartUtils.setupBarChart(barChartWeekly);
    }

    private void setupViewModel() {
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        
        dataViewModel.getRecentMoods(30).observe(getViewLifecycleOwner(), moods -> {
            recentMoods = moods;
            totalRecords = moods.size();
            tvTotalRecords.setText(String.valueOf(totalRecords));
            updateMoodLineChart();
            updateWeeklyBarChart();
            updateBestMood();
            updateStreak();
        });
        
        dataViewModel.getMoodStatistics().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null && !stats.isEmpty()) {
                updateMoodPieChart(stats);
                updateMoodCountCards(stats);
            }
        });
        
        dataViewModel.getAverageMoodScore().observe(getViewLifecycleOwner(), average -> {
            if (average != null) {
                tvAverageScore.setText(String.format("%.1f", average));
                updateMoodDescription(average);
            } else {
                tvAverageScore.setText("--");
                tvMoodDesc.setText("开始记录心情吧！");
            }
        });
        
        long startOfMonth = DateUtils.getStartOfMonth();
        dataViewModel.loadAverageMoodScore(startOfMonth);
        dataViewModel.loadMoodStatistics();
    }

    private void updateMoodLineChart() {
        if (recentMoods == null || recentMoods.isEmpty()) {
            lineChart.clear();
            return;
        }
        
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < recentMoods.size(); i++) {
            MoodRecord mood = recentMoods.get(i);
            entries.add(new Entry(i, mood.getMoodScore()));
        }
        
        ChartUtils.updateMoodLineChart(lineChart, entries, "心情趋势");
    }

    private void updateMoodPieChart(Map<String, Integer> stats) {
        List<Integer> colors = ChartUtils.getMoodColors();
        ChartUtils.updateMoodPieChart(pieChart, stats, colors);
    }

    private void updateMoodCountCards(Map<String, Integer> stats) {
        int happyCount = stats.getOrDefault(MoodRecord.MoodType.HAPPY, 0);
        int excitedCount = stats.getOrDefault(MoodRecord.MoodType.EXCITED, 0);
        int calmCount = stats.getOrDefault(MoodRecord.MoodType.NORMAL, 0);
        
        tvHappyCount.setText(String.valueOf(happyCount));
        tvExcitedCount.setText(String.valueOf(excitedCount));
        tvCalmCount.setText(String.valueOf(calmCount));
    }

    private void updateMoodDescription(float average) {
        String description;
        if (average >= 9.0) {
            description = "太棒了！你最近心情非常好！🎉";
        } else if (average >= 7.0) {
            description = "很不错！继续保持好心情！✨";
        } else if (average >= 5.0) {
            description = "还不错，给自己一些鼓励！💪";
        } else if (average >= 3.0) {
            description = "有些低落，试着做些让自己开心的事吧！🌸";
        } else {
            description = "最近不太顺？记得要照顾好自己！❤️";
        }
        tvMoodDesc.setText(description);
    }

    private void updateWeeklyBarChart() {
        if (recentMoods == null || recentMoods.isEmpty()) {
            barChartWeekly.clear();
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        
        float[] dailyScores = new float[7];
        int[] dailyCounts = new int[7];
        
        for (MoodRecord mood : recentMoods) {
            long time = mood.getCreatedAt();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeInMillis(time);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 2;
            if (dayOfWeek < 0) dayOfWeek = 6;
            
            if (dayOfWeek >= 0 && dayOfWeek < 7) {
                dailyScores[dayOfWeek] += mood.getMoodScore();
                dailyCounts[dayOfWeek]++;
            }
        }
        
        for (int i = 0; i < 7; i++) {
            float avgScore = dailyCounts[i] > 0 ? dailyScores[i] / dailyCounts[i] : 0f;
            entries.add(new BarEntry(i, avgScore));
        }
        
        List<Integer> colors = ChartUtils.getMoodColors();
        ChartUtils.updateBarChart(barChartWeekly, entries, "每日平均心情", colors);
    }

    private void updateBestMood() {
        if (recentMoods == null || recentMoods.isEmpty()) {
            tvBestMood.setText("暂无记录，开始记录你的心情吧！");
            return;
        }
        
        MoodRecord bestMood = null;
        int maxScore = -1;
        
        for (MoodRecord mood : recentMoods) {
            if (mood.getMoodScore() > maxScore) {
                maxScore = mood.getMoodScore();
                bestMood = mood;
            }
        }
        
        if (bestMood != null) {
            String moodType = getMoodEmoji(bestMood.getMoodType());
            String content = bestMood.getContent() != null ? bestMood.getContent() : "没有备注";
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日", java.util.Locale.getDefault());
            String date = sdf.format(new java.util.Date(bestMood.getCreatedAt()));
            tvBestMood.setText(String.format("%s %s\n记录于：%s\n备注：%s", 
                moodType, "心情满分的一天！", date, content));
        }
    }

    private void updateStreak() {
        if (recentMoods == null || recentMoods.isEmpty()) {
            tvStreak.setText("0");
            return;
        }
        
        int streak = 1;
        long lastDate = recentMoods.get(0).getCreatedAt();
        
        for (int i = 1; i < recentMoods.size(); i++) {
            long currentDate = recentMoods.get(i).getCreatedAt();
            long daysBetween = (lastDate - currentDate) / (1000 * 60 * 60 * 24);
            
            if (daysBetween == 1) {
                streak++;
            } else if (daysBetween > 1) {
                break;
            }
            lastDate = currentDate;
        }
        
        tvStreak.setText(String.valueOf(streak));
    }

    private String getMoodEmoji(String moodType) {
        switch (moodType) {
            case "HAPPY": return "😊";
            case "EXCITED": return "🤩";
            case "NORMAL": return "😐";
            case "SAD": return "😢";
            case "ANGRY": return "😠";
            default: return "😊";
        }
    }
}

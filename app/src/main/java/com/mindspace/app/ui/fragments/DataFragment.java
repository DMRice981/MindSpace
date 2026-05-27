package com.mindspace.app.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.example.mindspace.R;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.utils.ChartUtils;
import com.mindspace.app.utils.DateUtils;
import com.mindspace.app.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFragment extends Fragment {
    
    private DataViewModel dataViewModel;
    
    private LineChart lineChart;
    private PieChart pieChart;
    private TextView tvAverageScore;
    
    private List<MoodRecord> recentMoods = new ArrayList<>();

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
        tvAverageScore = view.findViewById(R.id.tv_average_score);
    }

    private void setupCharts() {
        ChartUtils.setupLineChart(lineChart);
        ChartUtils.setupPieChart(pieChart);
    }

    private void setupViewModel() {
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        
        dataViewModel.getRecentMoods(30).observe(getViewLifecycleOwner(), moods -> {
            recentMoods = moods;
            updateMoodLineChart();
        });
        
        dataViewModel.getMoodStatistics().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null && !stats.isEmpty()) {
                updateMoodPieChart(stats);
            }
        });
        
        dataViewModel.getAverageMoodScore().observe(getViewLifecycleOwner(), average -> {
            if (average != null) {
                tvAverageScore.setText(String.format("平均心情指数: %.1f", average));
            } else {
                tvAverageScore.setText("平均心情指数: --");
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
}

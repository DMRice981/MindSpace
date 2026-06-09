package com.mindspace.app.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartUtils {

    public static final String COLOR_PRIMARY = "#7C4DFF";
    public static final String COLOR_ACCENT = "#FF6D9A";
    public static final String COLOR_SUCCESS = "#4CAF50";
    public static final String COLOR_WARNING = "#FF9800";
    public static final String COLOR_INFO = "#2196F3";

    public static void setupLineChart(LineChart chart) {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(false);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#757575"));
        
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#E0E0E0"));
        leftAxis.setTextColor(Color.parseColor("#757575"));
        
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(Color.parseColor("#757575"));
        chart.animateX(1500);
    }

    public static void setupPieChart(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(Color.parseColor("#757575"));
    }

    public static void updateMoodLineChart(LineChart chart, List<Entry> entries, String label) {
        if (entries.isEmpty()) {
            chart.clear();
            return;
        }
        
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(Color.parseColor(COLOR_PRIMARY));
        dataSet.setCircleColor(Color.parseColor(COLOR_ACCENT));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.parseColor(COLOR_PRIMARY));
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor(COLOR_PRIMARY));
        dataSet.setFillAlpha(50);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    public static void updateCategoryPieChart(PieChart chart, Map<String, Integer> data, List<Integer> colors) {
        List<PieEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new PieEntry(entry.getValue(), getCategoryDisplayName(entry.getKey())));
            }
        }
        
        if (entries.isEmpty()) {
            chart.clear();
            chart.invalidate();
            return;
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "笔记分类");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.invalidate();
    }

    public static void updateMoodPieChart(PieChart chart, Map<String, Integer> data, List<Integer> colors) {
        List<PieEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new PieEntry(entry.getValue(), getMoodDisplayName(entry.getKey())));
            }
        }
        
        if (entries.isEmpty()) {
            chart.clear();
            chart.invalidate();
            return;
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "心情分布");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.invalidate();
    }

    private static String getCategoryDisplayName(String category) {
        switch (category) {
            case "WORK": return "工作";
            case "STUDY": return "学习";
            case "LIFE": return "生活";
            case "IDEA": return "创意";
            default: return category;
        }
    }

    private static String getMoodDisplayName(String mood) {
        switch (mood) {
            case "HAPPY": return "开心";
            case "EXCITED": return "兴奋";
            case "NORMAL": return "平静";
            case "SAD": return "难过";
            case "ANGRY": return "生气";
            default: return mood;
        }
    }

    public static List<Integer> getCategoryColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor(COLOR_ACCENT));
        colors.add(Color.parseColor(COLOR_PRIMARY));
        colors.add(Color.parseColor(COLOR_SUCCESS));
        colors.add(Color.parseColor(COLOR_WARNING));
        return colors;
    }

    public static List<Integer> getMoodColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor(COLOR_SUCCESS));
        colors.add(Color.parseColor(COLOR_WARNING));
        colors.add(Color.parseColor(COLOR_INFO));
        colors.add(Color.parseColor("#9C27B0"));
        colors.add(Color.parseColor(COLOR_ACCENT));
        return colors;
    }

    public static void setupBarChart(BarChart chart) {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#757575"));
        
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#E0E0E0"));
        leftAxis.setTextColor(Color.parseColor("#757575"));
        
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(Color.parseColor("#757575"));
        chart.animateY(1500);
    }

    public static void updateBarChart(BarChart chart, List<BarEntry> entries, String label, List<Integer> colors) {
        if (entries.isEmpty()) {
            chart.clear();
            return;
        }
        
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColors(colors);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.parseColor(COLOR_PRIMARY));
        
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        chart.setData(barData);
        chart.invalidate();
    }
}

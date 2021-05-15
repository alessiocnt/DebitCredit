package com.simoale.debitcredit.ui.graphs;

import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CircularGaugeChart implements Chart {

    AnyChartView anyChartView;
    CircularGauge circularGauge;

    Map<String, Integer> data;

    public CircularGaugeChart(AnyChartView anyChartView, Map<String, Integer>  data, String title){
        this.anyChartView = anyChartView;
        this.data = data;
        this.circularGauge = AnyChart.circular();

        setupCircular();
        setupCircles(title);
    }

    private void setupCircular() {
        circularGauge.data(GenerateDataSet());
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d);
        circularGauge.startAngle(0d);
        circularGauge.sweepAngle(270d);

        Circular xAxis = circularGauge.axis(0)
                .radius(500d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d);
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);
    }

    private SingleValueDataSet GenerateDataSet() {
        List<Integer> dataList = new ArrayList<>();
        this.data.forEach((k, v) -> {
            dataList.add(v);
        });
        return new SingleValueDataSet(dataList.toArray());
    }

    private void setupCircles(String title) {
        List<String> color = Arrays.asList("#64b5f6", "#1976d2", "#ef6c00", "#ffd54f", "#455a64");
        for (int currentIndex = 0; currentIndex < data.values().size(); currentIndex++){
            if(currentIndex >= 5){
                // Exit if reached maximum circularGauge capacity
                break;
            }
            circularGauge.label(currentIndex)
                    .text(data.keySet().toArray()[currentIndex] + ", <span style=\"\">" + data.get(data.keySet().toArray()[currentIndex]) + "%</span>")
                    .useHtml(true)
                    .hAlign(HAlign.CENTER)
                    .vAlign(VAlign.MIDDLE);
            circularGauge.label(currentIndex)
                    .anchor(Anchor.RIGHT_CENTER)
                    .padding(0d, 10d, 0d, 0d)
                    .height(17d / 2d + "%")
                    .offsetY("" + calculateRadius(currentIndex) + "%")
                    .offsetX(0d);
            Bar bar0 = circularGauge.bar(currentIndex);
            bar0.dataIndex(currentIndex);
            bar0.radius(calculateRadius(currentIndex));
            bar0.width(17d);
            bar0.fill(new SolidFill(color.get(currentIndex), 1d));
            bar0.stroke(null);
            bar0.zIndex(5d);
            Bar bar100 = circularGauge.bar(100 + currentIndex);
            bar100.dataIndex(5d);
            bar100.radius(calculateRadius(currentIndex));
            bar100.width(17d);
            bar100.fill(new SolidFill("#F5F4F4", 1d));
            bar100.stroke("1 #e5e4e4");
            bar100.zIndex(4d);
        }

        circularGauge.margin(50d, 50d, 50d, 50d);
        circularGauge.title()
                .text(title)
                .useHtml(true);
        circularGauge.title().enabled(true);
        circularGauge.title().hAlign(HAlign.CENTER);
        circularGauge.title()
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 20d, 0d);
    }

    private int calculateRadius(int currentIndex) {
        int dataSize = data.values().size();
        int k = 100/dataSize;
        return (dataSize - currentIndex) * k;
    }

    @Override
    public void instantiateChart() {
        this.anyChartView.setChart(circularGauge);
    }
}

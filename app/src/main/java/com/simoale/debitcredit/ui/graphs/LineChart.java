package com.simoale.debitcredit.ui.graphs;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;

import java.util.List;
import java.util.Map;

public class LineChart implements Chart {

    AnyChartView anyChartView;
    Cartesian cartesian;

    Map<String, List<DataEntry>> data;

    public LineChart(AnyChartView anyChartView, Map<String, List<DataEntry>> data, String title, String xLabel, String yLabel){
        this.anyChartView = anyChartView;
        this.data = data;
        this.cartesian = AnyChart.line();

        setupCartesian(title, xLabel, yLabel);
        setupLines();
    }

    private void setupLines() {
        this.data.forEach((k, v) -> {
            cartesian.line(v)
                    .name(k)
                    .hovered().markers().enabled(true)
                    .type(MarkerType.CIRCLE).size(4d);
            }
        );
    }

    private void setupCartesian(String title, String xLabel, String yLabel) {
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title(title);
        cartesian.yAxis(0).title(yLabel);
        cartesian.xAxis(0).title(xLabel);
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
    }

    @Override
    public void instantiateChart() {
        this.anyChartView.setChart(this.cartesian);
    }
}

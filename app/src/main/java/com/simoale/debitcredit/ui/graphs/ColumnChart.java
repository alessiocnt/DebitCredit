package com.simoale.debitcredit.ui.graphs;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.List;

public class ColumnChart implements Chart {

    AnyChartView anyChartView;
    Cartesian cartesian;
    Column column;

    List<DataEntry> data;

    public ColumnChart(AnyChartView anyChartView, List<DataEntry> data, String title, String xLabel, String yLabel){
        this.anyChartView = anyChartView;
        this.data = data;
        this.cartesian = AnyChart.column();
        this.column = cartesian.column(data);

        setupColumn();
        setupCartesian(title, xLabel, yLabel);
    }

    private void setupCartesian(String title, String xLabel, String yLabel) {
        cartesian.animation(true);
        cartesian.title(title);

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title(xLabel);
        cartesian.yAxis(0).title(yLabel);
    }

    private void setupColumn() {
        this.column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");
    }

    public Chart pushSingleData(DataEntry data) {
        this.data.add(data);
        this.column = cartesian.column(this.data);
        return this;
    }

    public Chart pushData(List<DataEntry> data) {
        this.data.addAll(data);
        this.column = cartesian.column(this.data);
        return this;
    }

    @Override
    public void instantiateChart() {
        this.anyChartView.setChart(this.cartesian);
    }
}

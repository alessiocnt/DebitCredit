package com.simoale.debitcredit.ui.graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.simoale.debitcredit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphsFragment extends Fragment {

    // private GraphsViewModel graphsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // graphsViewModel = new ViewModelProvider(this).get(GraphsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_graphs, container, false);
       /** final TextView textView = root.findViewById(R.id.text_home);
        graphsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });**/


        AnyChartView lineChartView = root.findViewById(R.id.line_chart);
        lineChartView.setProgressBar(root.findViewById(R.id.line_chart_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(lineChartView);

        List<DataEntry> brandySeriesData = new ArrayList<>();
        brandySeriesData.add(new ValueDataEntry("1986", 3.6));
        brandySeriesData.add(new ValueDataEntry("1987", 7.1));
        brandySeriesData.add(new ValueDataEntry("1988", 8.5));
        brandySeriesData.add(new ValueDataEntry("1989", 9.2));
        brandySeriesData.add(new ValueDataEntry("1990", 10.1));
        brandySeriesData.add(new ValueDataEntry("1991", 11.6));
        brandySeriesData.add(new ValueDataEntry("1992", 16.4));
        brandySeriesData.add(new ValueDataEntry("1993", 18.0));
        brandySeriesData.add(new ValueDataEntry("1994", 13.2));
        brandySeriesData.add(new ValueDataEntry("1995", 12.0));
        brandySeriesData.add(new ValueDataEntry("1996", 3.2));
        brandySeriesData.add(new ValueDataEntry("1997", 4.1));
        brandySeriesData.add(new ValueDataEntry("1998", 6.3));
        brandySeriesData.add(new ValueDataEntry("1999", 9.4));
        brandySeriesData.add(new ValueDataEntry("2000", 11.5));

        List<DataEntry> wiskySeriesData = new ArrayList<>();
        wiskySeriesData.add(new ValueDataEntry("1986", 2.3));
        wiskySeriesData.add(new ValueDataEntry("1987", 4.0));
        wiskySeriesData.add(new ValueDataEntry("1988", 6.2));
        wiskySeriesData.add(new ValueDataEntry("1989", 11.8));
        wiskySeriesData.add(new ValueDataEntry("1990", 13.0));
        wiskySeriesData.add(new ValueDataEntry("1991", 13.9));
        wiskySeriesData.add(new ValueDataEntry("1992", 18.0));
        wiskySeriesData.add(new ValueDataEntry("1993", 23.3));
        wiskySeriesData.add(new ValueDataEntry("1994", 24.7));
        wiskySeriesData.add(new ValueDataEntry("1995", 18.0));
        wiskySeriesData.add(new ValueDataEntry("1996", 15.1));
        wiskySeriesData.add(new ValueDataEntry("1997", 11.3));
        wiskySeriesData.add(new ValueDataEntry("1998", 14.2));
        wiskySeriesData.add(new ValueDataEntry("1999", 13.7));
        wiskySeriesData.add(new ValueDataEntry("2000", 9.9));

        List<DataEntry> tequilaSeriesData = new ArrayList<>();
        tequilaSeriesData.add(new ValueDataEntry("1986", 3.3));
        tequilaSeriesData.add(new ValueDataEntry("1987", 5.0));
        tequilaSeriesData.add(new ValueDataEntry("1988", 7.2));
        tequilaSeriesData.add(new ValueDataEntry("1989", 12.8));
        tequilaSeriesData.add(new ValueDataEntry("1990", 14.0));
        tequilaSeriesData.add(new ValueDataEntry("1991", 14.9));
        tequilaSeriesData.add(new ValueDataEntry("1992", 19.0));
        tequilaSeriesData.add(new ValueDataEntry("1993", 24.3));
        tequilaSeriesData.add(new ValueDataEntry("1994", 25.7));
        tequilaSeriesData.add(new ValueDataEntry("1995", 19.0));
        tequilaSeriesData.add(new ValueDataEntry("1996", 16.1));
        tequilaSeriesData.add(new ValueDataEntry("1997", 12.3));
        tequilaSeriesData.add(new ValueDataEntry("1998", 15.2));
        tequilaSeriesData.add(new ValueDataEntry("1999", 14.7));
        tequilaSeriesData.add(new ValueDataEntry("2000", 10.9));

        Map<String, List<DataEntry>> lineData = new HashMap<>();
        lineData.put("Brandy", brandySeriesData);
        lineData.put("Whiskey", wiskySeriesData);
        lineData.put("Tequila", tequilaSeriesData);

        Chart lineChart = new LineChart(lineChartView, lineData, "Trend of Sales", "Years", "Bottles Sold");
        lineChart.instantiateChart();



        /***************************/

        AnyChartView columnChartView = root.findViewById(R.id.candle_chart);
        columnChartView.setProgressBar(root.findViewById(R.id.candle_chart_progress_bar));
        APIlib.getInstance().setActiveAnyChartView(columnChartView);


        List<DataEntry> columnData = new ArrayList<>();
        columnData.add(new ValueDataEntry("Rouge", 80540));
        columnData.add(new ValueDataEntry("Foundation", 94190));
        columnData.add(new ValueDataEntry("Mascara", 102610));
        columnData.add(new ValueDataEntry("Lip gloss", 110430));
        columnData.add(new ValueDataEntry("Lipstick", 128000));
        columnData.add(new ValueDataEntry("Nail polish", 143760));
        columnData.add(new ValueDataEntry("Eyebrow pencil", 170670));
        columnData.add(new ValueDataEntry("Eyeliner", 213210));
        columnData.add(new ValueDataEntry("Eyeshadows", 249980));

        Chart columnChart = new ColumnChart(columnChartView, columnData, "Top 10 Cosmetic", "prod", "revenue");
        columnChart.instantiateChart();

        return root;
    }

}
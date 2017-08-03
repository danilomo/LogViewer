/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.logviewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author danilo
 */
public final class DynamicChart {

    private DynamicTimeSeriesCollection dataset;
    private JFreeChart chart;
    private float yLower;
    private float yUpper;
    private float value;
    private String title;
    private String xLab;
    private String yLab;
    private static final int FAST = 1000;
    private static final int COUNT = 60;
    private final JPanel panel;

    private DynamicChart(float yLower, float yUpper, String title, String xLab, String yLab, JPanel panel) {
        this.yLower = yLower;
        this.yUpper = yUpper;
        this.title = title;
        this.xLab = xLab;
        this.yLab = yLab;
        this.panel = panel;

        value = 0;

        initSeries();
        
    }
    
    public void startUpdating(){
        initPanel();

        startTimer();
    }

    public void registerAtProvider(ValuesProvider vp) {
        vp.registerListener((d) -> {
            updateValue((float) d);
        });
    }

    private void initSeries() {
        dataset = new DynamicTimeSeriesCollection(1, COUNT * 2, new Second());
        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        dataset.addSeries(getInitialData(), 0, "");
        chart = createChart(dataset);
    }

    private JFreeChart createChart(final XYDataset dataset) {

        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title, xLab, yLab, dataset, true, true, false);

        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(-yLower, yUpper);
        return result;
    }

    public synchronized void updateValue(float val) {
        this.value = val;
    }

    private void initPanel() {
        assert (panel.getComponents().length > 0);

        panel.setLayout(new BorderLayout());

        panel.add(new ChartPanel(chart), BorderLayout.CENTER);

    }

    private void startTimer() {
        Timer timer = new Timer(FAST, new ActionListener() {

            float[] newData = new float[1];

            @Override
            public void actionPerformed(ActionEvent e) {
                newData[0] = value;
                dataset.advanceTime();
                dataset.appendData(newData);
            }
        });

        timer.start();
    }

    private float[] getInitialData() {
        float[] a = new float[COUNT];
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
        return a;
    }

    public static class Builder {

        private float yLower;
        private float yUpper;
        private String title;
        private String xLab;
        private String yLab;
        private JPanel panel;

        public Builder() {
            xLab = "";
            yLab = "";
            title = "";
            yLower = 0;
            yUpper = 100;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setxLab(String xLab) {
            this.xLab = xLab;
            return this;
        }

        public Builder setyLab(String yLab) {
            this.yLab = yLab;
            return this;
        }

        public Builder setYRange(float yLower, float yUpper) {
            this.yLower = yLower;
            this.yUpper = yUpper;
            return this;
        }

        public Builder setPanel(JPanel panel) {
            this.panel = panel;
            return this;
        }

        public DynamicChart generate() {

            if (panel == null) {
                throw new RuntimeException("JPanel not configured");
            }

            if (yUpper < yLower) {
                throw new RuntimeException("Invalide y range.");
            }

            DynamicChart dc = new DynamicChart(yLower, yUpper, title, xLab, yLab, panel);

            return dc;
        }

    }

}

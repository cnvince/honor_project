package com.evaluation;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


@SuppressWarnings("serial")
public class lineChart extends ApplicationFrame {

	final XYSeriesCollection dataset = new XYSeriesCollection();

	public lineChart(final String title) {
		super(title);
	}

	public void addData(String[] NDCG) {
		final XYSeries series;
		series = new XYSeries(NDCG[0]);
		for (int j = 1; j < NDCG.length; j++) {
			double cN = Double.parseDouble(NDCG[j]);
			series.add(j, cN);
		}
		dataset.addSeries(series);
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("NDCG Chart", // chart
																				// title
				"rank", // x axis label
				"NDCG", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(0.00, 1.00);
		domain.setTickUnit(new NumberTickUnit(5));
		// domain.setVerticalTickLabels(true);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		range.setRange(0.0, 1.2);
		range.setTickUnit(new NumberTickUnit(0.1));

		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	public void output(String query,String path) {
		final JFreeChart chart = createChart(dataset);
		File file = new File(path);
		try {
			ChartUtilities.saveChartAsPNG(file, chart, 640, 480);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
	}

}

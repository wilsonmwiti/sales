package id.co.beton.saleslogistic_trackingsystem.Chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Class AxisValueFormatter
 */
public class AxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat decimalFormat;

    public AxisValueFormatter(){
        decimalFormat = new DecimalFormat("######");
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return decimalFormat.format(value)+ "";
    }
}

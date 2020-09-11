package id.co.beton.saleslogistic_trackingsystem.Chart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Class ValueFormatter
 */
public class ValueFormatter implements IValueFormatter {
    private DecimalFormat decimalFormat;

    public ValueFormatter(){
        decimalFormat = new DecimalFormat("######");
    }
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return decimalFormat.format(value) + "";
    }
}

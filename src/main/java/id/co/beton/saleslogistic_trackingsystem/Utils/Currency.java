package id.co.beton.saleslogistic_trackingsystem.Utils;

import java.text.DecimalFormat;

/**
 * Class Currency
 * The type Currency.
 */
public class Currency {
    private static Currency instance = null;

    /**
     * Instantiates a new Currency.
     */
    public Currency() {
    }

    /**
     * Get instance currency.
     *
     * @return the currency
     */
    public static Currency getInstance(){
        if(instance==null){
            instance = new Currency();
        }

        return instance;
    }

    /**
     * function to convert double to decimal
     *
     * @param price the price
     * @return string string
     */
    public static String doublePriceWithDecimal (double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    /**
     * function to convert integer to decimal
     *
     * @param price the price
     * @return string string
     */
    public static String priceWithDecimal (int price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    /**
     * function to convert integer to without decimal
     *
     * @param price the price
     * @return string string
     */
    public static String priceWithoutDecimal (int price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    /**
     * function to convert integer price to string
     *
     * @param price the price
     * @return string string
     */
    public static String priceToString(int price) {
        String toShow = priceWithoutDecimal(price);
        if (toShow.indexOf(".") > 0) {
            return priceWithDecimal(price);
        } else {
            return priceWithoutDecimal(price);
        }
    }
}

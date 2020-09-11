package id.co.beton.saleslogistic_trackingsystem.Printer;

/**
 * Class UnicodeFormatter
 *
 */
public class UnicodeFormatter {
    /**
     * Returns hex String representation of byte b
     * @param b
     * @return string
     */
    static public String byteToHex(byte b) {
        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }

    /**
     * Returns hex String representation of char c
     * @param c
     * @return string
     */
    static public String charToHex(char c) {
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }
}

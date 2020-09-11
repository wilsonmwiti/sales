package id.co.beton.saleslogistic_trackingsystem.Nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import id.co.beton.saleslogistic_trackingsystem.Interfaces.ParsedNdefRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Class NdefMessageParser
 *
 */
public class NdefMessageParser {
    private NdefMessageParser() {
    }

    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();

        for (final NdefRecord record : records) {
            //only parse text
            if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            }else{
                elements.add(new ParsedNdefRecord() {
                    public String str() {
                        return new String(record.getPayload());
                    }
                });
            }
            /*if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
                elements.add(new ParsedNdefRecord() {
                    public String str() {
                        return new String(record.getPayload());
                    }
                });
            }*/
        }

        return elements;
    }
}

package id.co.beton.saleslogistic_trackingsystem.Utils;


/**
 * Class KonversiWaktu
 * to convert time
 */
public class KonversiWaktu {

    private int detik;

    /**
     * Instantiates a new Konversi waktu.
     *
     * @param detik the detik
     */
    public KonversiWaktu(int detik){
        this.detik = detik;
    }

    /**
     * Get waktu string.
     *
     * @return the string
     */
    public String getWaktu(){
        String hasil="";
        int menit;
        double jam, sisa1, sisa2;

        jam = detik/3600;
        sisa1 = detik%3600;

        menit = (int) sisa1/60;
        sisa2 = sisa1%60;

        //jam
        if(jam!=0){
            hasil += jam+" Jam ";
        }
        //menit
        if(menit!=0){
            hasil += menit+" Menit ";
        }
        //detik
//        if(sisa2!=0){
//            hasil += jam+" Detik";
//        }

        return hasil;
    }

}

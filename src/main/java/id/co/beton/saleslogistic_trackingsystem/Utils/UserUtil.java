package id.co.beton.saleslogistic_trackingsystem.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.User;


/**
 * Class UserUtil extend to class Utils
 */
public class UserUtil extends Utils {
    private static UserUtil instance;

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static UserUtil getInstance(Context context) {
        if (instance == null) {
            instance = new UserUtil();
            instance.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return instance;
    }

    /**
     * Get version apps string.
     *
     * @param context the context
     * @return the string
     */
    public String getVersionApps(Context context){
        String versionApps = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            versionApps = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionApps;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return !getUsername().isEmpty();
    }

    /**
     * function to set parameter after user successfully login
     *
     * @param user User
     */
    public void signIn(User user) {
        try{
            if(user.getId()!=null){
                setIntProperty(Constants.USER_ID,user.getId());
            }else{
                setIntProperty(Constants.USER_ID,1);
            }
            //setStringProperty(Constants.USER_EMAIL,user.getEmail());
            setStringProperty(Constants.USERNAME,user.getUsername());
            setStringProperty(Constants.USER_ROLE,user.getMobilePrivilege().toLowerCase());
            setStringProperty(Constants.JWTACCESSTOKEN,user.getJwtAccessToken());
            setStringProperty(Constants.NAME,user.getName());
            setStringProperty(Constants.NIP,user.getNip());
            setStringProperty(Constants.PHONE,user.getPhone());
            //setIntProperty(Constants.RES_TIME,user.getSetting().getMaxBreakTime());
            setRestTime(user.getSetting().getMaxBreakTime());
            setIntProperty(Constants.MAX_IDLE_TIME,user.getSetting().getMaxIdleTime());
            setIntProperty(Constants.MAX_UNLOADING,user.getSetting().getMaxLengthUnloading());
            setIntProperty(Constants.MAX_VISIT_TIME,user.getSetting().getMaxLengthVisitTime());
            setIntProperty(Constants.BRANCH_ID,user.getBranch().getId());

            // for check last login, jika tgl last_login < hari ini (kemarin), then auto logout
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            setStringProperty(Constants.LAST_LOGIN,dateFormat.format(date));

            // alert
            setIntProperty(Constants.ALERT_WRONG_ROUTE, user.getSetting().getAlertWrongRoute());
            setIntProperty(Constants.ALERT_BREAK_TIME, user.getSetting().getAlertBreakTime());

            // collector
            setIntProperty(Constants.IS_COLLECTOR_ONLY, user.getIsCollectorOnly());
            setIntProperty(Constants.IS_CAN_COLLECT, user.getIsCanCollect());

            if(user.getDivision()==null || user.getDivision().getId()==null){
                setIntProperty(Constants.DIVISION_ID,0);
            }else{
                setIntProperty(Constants.DIVISION_ID,user.getDivision().getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Get rest time int.
     *
     * @return the int
     */
    public int getRestTime(){
        return getIntProperty(Constants.RES_TIME);
    }

    /**
     * Set rest time.
     *
     * @param restTime the rest time
     */
    public void setRestTime(int restTime){
        setIntProperty(Constants.RES_TIME,restTime);
    }

    /**
     * Get long break time int.
     *
     * @return the int
     */
    public int getLongBreakTime(){
        return getIntProperty(Constants.LONG_BRAEK_TIME);
    }

    /**
     * Set long break time.
     *
     * @param longBreakTime the long break time
     */
    public void setLongBreakTime(int longBreakTime){
        setIntProperty(Constants.LONG_BRAEK_TIME,longBreakTime);
    }

    /**
     * function to set constant START_VISIT & START_VISIT_TIME_SERVICE
     * after sales Checin at customer
     */
    public void setStarVisit(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        setStringProperty(Constants.START_VISIT,dateFormat.format(calendar.getTime()));
        setBooleanProperty(Constants.START_VISIT_TIME_SERVICE,true);
    }

    /**
     * function to check status idle time
     *
     * @param lat the lat
     * @param lng the lng
     * @return boolean boolean
     */
    public boolean  checkIdleTime(double lat, double lng){
        //cek udah pernah set atau belum jika belum maka set jika sudah cek sudah berapa lama di koordinat itu

        if(getFloatProperty(Constants.LAST_LAT)!=0){
            //cek jarak koordinat lama dng yang baru jika jarak lebih dr 10 meter di anggap gerak dan ubah koordinat yg lama
            DistanceCalculator distanceCalculator = new DistanceCalculator();
            double jarak = distanceCalculator.greatCircleInMeters(new LatLng(lat,lng),new LatLng(getFloatProperty(Constants.LAST_LAT),getFloatProperty(Constants.LAST_LNG)));

            if(jarak>10){
                setLastLng(lng);
                setLastLat(lat);
                setlastDatePosition();
            }else{
                try{
                    Date date_current, date_visit_time;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String strDate = simpleDateFormat.format(calendar.getTime());
                    date_current = simpleDateFormat.parse(strDate);
                    date_visit_time = simpleDateFormat.parse(getStartLastPostion());

                    long diff = date_current.getTime() - date_visit_time.getTime();
                    long seconds  =TimeUnit.MILLISECONDS.toSeconds(diff);
                    if(seconds > getMaxIdle()){
                        //notif idle time
                        return true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            setLastLng(lng);
            setLastLat(lat);
            setlastDatePosition();
        }
        return false;
    }

    /**
     * function to set idle Time
     *
     * @return long
     */
    public long idleTime(){
        try{
            Date date_current, date_visit_time;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String strDate = simpleDateFormat.format(calendar.getTime());
            date_current = simpleDateFormat.parse(strDate);
            date_visit_time = simpleDateFormat.parse(getStartLastPostion());

            long diff = date_current.getTime() - date_visit_time.getTime();
            long seconds  =TimeUnit.MILLISECONDS.toSeconds(diff);
            return seconds;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    private void setLastLat(double lat){
        setFloatProperty(Constants.LAST_LAT,(float)lat);
    }

    private void setLastLng(double lng){
        setFloatProperty(Constants.LAST_LNG,(float)lng);
    }

    private void setlastDatePosition(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        setStringProperty(Constants.TIME_LAST_POSITION,dateFormat.format(calendar.getTime()));
    }

    private String getStartVisit(){
        return getStringProperty(Constants.START_VISIT);
    }

    private String getStartLastPostion(){
        return getStringProperty(Constants.TIME_LAST_POSITION);
    }

    /**
     * Set star unloading.
     */
    public void setStarUnloading(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        setStringProperty(Constants.START_UNLOADING,dateFormat.format(calendar.getTime()));
        setBooleanProperty(Constants.START_VISIT_TIME_SERVICE,true);
    }

    /**
     * Set time visit customer.
     */
    public void setTimeVisitCustomer(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        setStringProperty(Constants.START_VISIT_CUSTOMER,dateFormat.format(calendar.getTime()).toString());
    }

    /**
     * Get time visit customer string.
     *
     * @return the string
     */
    public String getTimeVisitCustomer(){
        return getStringProperty(Constants.START_VISIT_CUSTOMER);
    }

    /**
     * function to status sales can tap out
     * time minimum visit is set to Constant MIN_VISIT_CUSTOMER
     * sales can Tap Out if time more then in Constant MIN_VISIT_CUSTOMER
     *
     * @return boolean boolean
     */
    public boolean checkTimeVisitCustomer(){
        boolean canTapOut = false;
        try{
            Date date_current, date_visit_time;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String strDate = simpleDateFormat.format(calendar.getTime());
            date_current = simpleDateFormat.parse(strDate);
            date_visit_time = simpleDateFormat.parse(getTimeVisitCustomer());

            long diff = date_current.getTime() - date_visit_time.getTime();
            long seconds  =TimeUnit.MILLISECONDS.toSeconds(diff);
            if(seconds > Constants.MIN_VISIT_CUSTOMER){
                canTapOut = true;
                // function below is moved to InfoFragment on processCheckoutSubmit
                // setStringProperty(Constants.START_VISIT_CUSTOMER,"");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return canTapOut;
    }

    /**
     * Remove start visit.
     */
    public void removeStartVisit(){
        setBooleanProperty(Constants.START_VISIT_TIME_SERVICE,false);
    }

    private String getStartUnloading(){
        return getStringProperty(Constants.START_UNLOADING);
    }

    /**
     * Get max visit time int.
     *
     * @return the int
     */
    public int getMaxVisitTime(){
        return  getIntProperty(Constants.MAX_VISIT_TIME)+getIntProperty(Constants.ADDITIONAL_TIME_PERMISSION);
    }

    /**
     * Get max unloading int.
     *
     * @return the int
     */
    public int getMaxUnloading(){
        return  getIntProperty(Constants.MAX_UNLOADING)+getIntProperty(Constants.ADDITIONAL_TIME_PERMISSION);
    }

    private int getMaxIdle(){
        return  getIntProperty(Constants.MAX_IDLE_TIME);
    }

    /**
     * check max time visit sales, maximum time visit set on function getMaxVisitTime
     * return true if time visit more then maximum
     *
     * @return boolean boolean
     */
    public boolean checkMaxVisitTime(){
        try{
            Date date_current, date_visit_time;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String strDate = simpleDateFormat.format(calendar.getTime());
            date_current = simpleDateFormat.parse(strDate);
            date_visit_time = simpleDateFormat.parse(getStartVisit());

            long diff = date_current.getTime() - date_visit_time.getTime();
            long minutes  =TimeUnit.MILLISECONDS.toSeconds(diff);
            if(minutes > getMaxVisitTime()){
                //notif batas visit time melebihi
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * check max time unloading logistic, maximum unloading logistic set on function getMaxUnloading
     * return true if time unloading logistic more then maximum
     *
     * @return boolean boolean
     */
    public boolean checkMaxUnloadingTime(){
        try{
            Date date_current, date_visit_time;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String strDate = simpleDateFormat.format(calendar.getTime());
            date_current = simpleDateFormat.parse(strDate);
            date_visit_time = simpleDateFormat.parse(getStartUnloading());

            long diff = date_current.getTime() - date_visit_time.getTime();
            long minutes  =TimeUnit.MILLISECONDS.toSeconds(diff);
            if(minutes > getMaxUnloading()){
                //notif batas unloading time melebihi
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * function to check time visit on long
     *
     * @return long long
     */
    public long getLongVisit(){
        try{
            Date date_current, date_visit_time;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String strDate = simpleDateFormat.format(calendar.getTime());
            date_current = simpleDateFormat.parse(strDate);
            if(getRoleUser().equals(Constants.ROLE_DRIVER)){
                date_visit_time = simpleDateFormat.parse(getStartUnloading());
            }else{
                date_visit_time = simpleDateFormat.parse(getStartVisit());
            }

            long diff = date_current.getTime() - date_visit_time.getTime();
            long seconds  =TimeUnit.MILLISECONDS.toSeconds(diff);

            return seconds;

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get last login string.
     *
     * @return the string
     */
    public String getLastLogin(){
        return getStringProperty(Constants.LAST_LOGIN);
    }

    /**
     * Sign out.
     */
    public void signOut() {
        reset();
    }

    /**
     * Get jwtt oken string.
     *
     * @return the string
     */
    public String getJWTTOken(){
        return getStringProperty(Constants.JWTACCESSTOKEN);
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return getStringProperty(Constants.USER_TOKEN);
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return getStringProperty(Constants.USER_EMAIL);
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return getStringProperty(Constants.USERNAME);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return getStringProperty(Constants.NAME);
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return getStringProperty(Constants.PHONE);
    }

    /**
     * Gets nip.
     *
     * @return the nip
     */
    public String getNip() {
        return getStringProperty(Constants.NIP);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return getIntProperty(Constants.USER_ID);
    }

    /**
     * Get role user string.
     *
     * @return the string
     */
    public String getRoleUser(){
        return getStringProperty(Constants.USER_ROLE);
    }

    /**
     * Get nfc code string.
     *
     * @return the string
     */
    public String getNfcCode(){
        return getStringProperty(Constants.NFC_CODE);
    }

    /**
     * Set nfc code route.
     *
     * @param nfcCode the nfc code
     */
    public void setNfcCodeRoute(String nfcCode){
        setStringProperty(Constants.NFC_CODE_ROUTE, nfcCode);
    }

    /**
     * Get nfc code route string.
     *
     * @return the string
     */
    public String getNfcCodeRoute(){
        return getStringProperty(Constants.NFC_CODE_ROUTE);
    }

    /**
     * Get branch id integer.
     *
     * @return the integer
     */
    public Integer getBranchId(){
        return getIntProperty(Constants.BRANCH_ID);
    }

    /**
     * Get division id integer.
     *
     * @return the integer
     */
    public Integer getDivisionId(){
        return getIntProperty(Constants.DIVISION_ID);
    }

    /**
     * Set status break time.
     *
     * @param statusBreakTime the status break time
     */
    public void setStatusBreakTime(boolean statusBreakTime){
        setBooleanProperty(Constants.STATUS_BREAK_TIME,statusBreakTime);
    }

    /**
     * Get status break time boolean.
     *
     * @return the boolean
     */
    public boolean getStatusBreakTime(){
        return  getBooleanProperty(Constants.STATUS_BREAK_TIME);
    }

    /**
     * Set status stop all service running.
     *
     * @param statusBreakTime the status break time
     */
    public void setStatusStopAllServiceRunning(boolean statusBreakTime){
        setBooleanProperty(Constants.STATUS_ALL_SERVICE_RUNNING,statusBreakTime);
    }

    /**
     * Get status stop all service running boolean.
     *
     * @return the boolean
     */
    public boolean getStatusStopAllServiceRunning(){
        return  getBooleanProperty(Constants.STATUS_ALL_SERVICE_RUNNING);
    }

    /**
     * Set index aktif.
     *
     * @param indexAktif the index aktif
     */
    public void setIndexAktif(int indexAktif){
        setIntProperty(Constants.INDEX_AKTIF, (int) indexAktif);
    }

    /**
     * Get index aktif int.
     *
     * @return the int
     */
    public int getIndexAktif(){
        return getIntProperty(Constants.INDEX_AKTIF);
    }

    /**
     * Set lat lng customer.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setLatLngCustomer(double lat,double lng){
        setFloatProperty(Constants.START_LAT_CUSTOMER,(float)lat);
        setFloatProperty(Constants.START_LNG_CUSTOMER,(float)lng);
    }

    /**
     * Get lat customer double.
     *
     * @return the double
     */
    public double getLatCustomer(){
        return (double) getFloatProperty(Constants.START_LAT_CUSTOMER);
    }

    /**
     * Get lng customer double.
     *
     * @return the double
     */
    public double getLngCustomer(){
        return (double) getFloatProperty(Constants.START_LNG_CUSTOMER);
    }

    /**
     * Set koordinar awal.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinarAwal(double lat,double lng){
        setFloatProperty(Constants.LAT_START, (float) lat);
        setFloatProperty(Constants.LNG_START, (float) lng);
    }

    /**
     * Set koordinar tengah.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinarTengah(double lat,double lng){
        setFloatProperty(Constants.LAT_MIDLE, (float) lat);
        setFloatProperty(Constants.LNG_MIDLE, (float) lng);
    }

    /**
     * Set koordinar akhir.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinarAkhir(double lat,double lng){
        setFloatProperty(Constants.LAT_END, (float) lat);
        setFloatProperty(Constants.LNG_END, (float) lng);
    }

    /**
     * Get koordinat awal float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatAwal(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_START);
        koor[1] =getFloatProperty(Constants.LNG_START);

        return koor;
    }

    /**
     * Get koordinat akhir float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatAkhir(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_END);
        koor[1] =getFloatProperty(Constants.LNG_END);

        return koor;
    }

    /**
     * Get koordinat tengah float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatTengah(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_MIDLE);
        koor[1] =getFloatProperty(Constants.LNG_MIDLE);

        return koor;
    }

    /**
     * Set koordinat office.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinatOffice(double lat, double lng){
        setFloatProperty(Constants.LAT_OFFICE, (float) lat);
        setFloatProperty(Constants.LNG_OFFICE, (float) lng);
    }

    /**
     * Set nf code office.
     *
     * @param nf_code the nf code
     */
    public void setNFCodeOffice(String nf_code){
        setStringProperty(Constants.NFC_CODE_OFFICE,nf_code);
    }

    /**
     * Get nf code office string.
     *
     * @return the string
     */
    public String getNFCodeOffice(){
        return getStringProperty(Constants.NFC_CODE_OFFICE);
    }

    /**
     * Get koordinat office float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatOffice(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_OFFICE);
        koor[1] =getFloatProperty(Constants.LNG_OFFICE);

        return koor;
    }

    /**
     * Setstatus stop service idle time.
     */
    public void setstatusStopServiceIdleTime(){
        setBooleanProperty(Constants.STATUS_STOP_SERVIE_IDLE_TIME,!getBooleanProperty(Constants.STATUS_STOP_SERVIE_IDLE_TIME));
    }

    /**
     * Get status stop service idle time boolean.
     *
     * @return the boolean
     */
    public boolean getStatusStopServiceIdleTime(){
        return getBooleanProperty(Constants.STATUS_STOP_SERVIE_IDLE_TIME);
    }

    /**
     * Set koordinat office start.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinatOfficeStart(double lat, double lng){
        setFloatProperty(Constants.LAT_OFFICE_START, (float) lat);
        setFloatProperty(Constants.LNG_OFFICE_START, (float) lng);
    }

    /**
     * Get koordinat office start float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatOfficeStart(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_OFFICE_START);
        koor[1] =getFloatProperty(Constants.LNG_OFFICE_START);

        return koor;
    }

    /**
     * Set koordinat office end.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinatOfficeEnd(double lat, double lng){
        setFloatProperty(Constants.LAT_OFFICE_END, (float) lat);
        setFloatProperty(Constants.LNG_OFFICE_END, (float) lng);
    }

    /**
     * Get koordinat office end float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatOfficeEnd(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_OFFICE_END);
        koor[1] =getFloatProperty(Constants.LNG_OFFICE_END);

        return koor;
    }

    /**
     * Set koordinat Custom Branch start.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinatCustomOfficeStart(double lat, double lng){
        setFloatProperty(Constants.LAT_CUSTOM_OFFICE_START, (float) lat);
        setFloatProperty(Constants.LNG_CUSTOM_OFFICE_START, (float) lng);
    }

    /**
     * Get koordinat Custom Branch start float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatCustomOfficeStart(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_CUSTOM_OFFICE_START);
        koor[1] =getFloatProperty(Constants.LNG_CUSTOM_OFFICE_START);

        return koor;
    }

    /**
     * Set koordinat Custom Branch end.
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void setKoordinatCustomOfficeEnd(double lat, double lng){
        setFloatProperty(Constants.LAT_CUSTOM_OFFICE_END, (float) lat);
        setFloatProperty(Constants.LNG_CUSTOM_OFFICE_END, (float) lng);
    }

    /**
     * Get koordinat Custom Branch end float [ ].
     *
     * @return the float [ ]
     */
    public float[] getKoordinatCustomOfficeEnd(){
        float[] koor=new float[2];
        koor[0] =getFloatProperty(Constants.LAT_CUSTOM_OFFICE_END);
        koor[1] =getFloatProperty(Constants.LNG_CUSTOM_OFFICE_END);

        return koor;
    }

    /**
     * Set Address Custom office Start
     * @param address
     */
    public void setAddressCustomOfficeStart(String address){
        setStringProperty(Constants.ADDRESS_CUSTOM_OFFICE_START, address);
    }

    /**
     * get Address custom start
     * @return
     */
    public String getAddressCustomOfficeStart(){
        return getStringProperty(Constants.ADDRESS_CUSTOM_OFFICE_START);
    }

    /**
     * set address custom end
     * @param address
     */
    public void setAddressCustomOfficeEnd(String address){
        setStringProperty(Constants.ADDRESS_CUSTOM_OFFICE_END, address);
    }

    /**
     * get address custom end
     * @return
     */
    public String getAddressCustomOfficeEnd(){
        return getStringProperty(Constants.ADDRESS_CUSTOM_OFFICE_END);
    }

    /**
     * Set nf code office start.
     *
     * @param nf_code the nf code
     */
    public void setNFCodeOfficeStart(String nf_code){
        setStringProperty(Constants.NFC_CODE_OFFICE_START,nf_code);
    }

    /**
     * Get nf code office start string.
     *
     * @return the string
     */
    public String getNFCodeOfficeStart(){
        return getStringProperty(Constants.NFC_CODE_OFFICE_START);
    }

    /**
     * Set nf code office end.
     *
     * @param nf_code the nf code
     */
    public void setNFCodeOfficeEnd(String nf_code){
        setStringProperty(Constants.NFC_CODE_OFFICE_END,nf_code);
    }

    /**
     * Get nf code office end string.
     *
     * @return the string
     */
    public String getNFCodeOfficeEnd(){
        return getStringProperty(Constants.NFC_CODE_OFFICE_END);
    }

    /**
     * Status alert wrong route boolean.
     *
     * @return the boolean
     */
// alert wrong route
    public boolean statusAlertWrongRoute(){
        return getIntProperty(Constants.ALERT_WRONG_ROUTE) == 1;
    }

    /**
     * Status alert break time boolean.
     *
     * @return the boolean
     */
// alert break_time
    public boolean statusAlertBreakTime(){
        return getIntProperty(Constants.ALERT_BREAK_TIME) == 1;
    }

    /**
     * Status alert idle boolean.
     *
     * @return the boolean
     */
// alert idle
    public boolean statusAlertIdle(){
        return getMaxIdle() > 0;
    }

    /**
     * Status alert visit time boolean.
     *
     * @return the boolean
     */
// alert visit time
    public boolean statusAlertVisitTime(){
        return getIntProperty(Constants.MAX_VISIT_TIME) > 0;
    }

    /**
     * Status alert unloading time boolean.
     *
     * @return the boolean
     */
// alert unloading time
    public boolean statusAlertUnloadingTime(){
        return getIntProperty(Constants.MAX_UNLOADING) > 0;
    }

    /**
     * Is collector only boolean.
     *
     * @return the boolean
     */
// collector
    public boolean isCollectorOnly(){
        return getIntProperty(Constants.IS_COLLECTOR_ONLY) == 1;
    }

    /**
     * Is can collect boolean.
     *
     * @return the boolean
     */
    public boolean isCanCollect(){
        return getIntProperty(Constants.IS_CAN_COLLECT) == 1;
    }

    /**
     * Set distance last location.
     *
     * @param lat the lat
     * @param lng the lng
     */
// distance
    public void setDistanceLastLocation(double lat, double lng){
        setLongProperty(Constants.LAST_LAT_DISTANCE, Double.doubleToRawLongBits(lat));
        setLongProperty(Constants.LAST_LNG_DISTANCE, Double.doubleToRawLongBits(lng));
    }
    private double[] getDistanceLastLocation(){
        double[] distance = new double[2];
        distance[0] = Double.longBitsToDouble(getLongProperty(Constants.LAST_LAT_DISTANCE));
        distance[1] = Double.longBitsToDouble(getLongProperty(Constants.LAST_LNG_DISTANCE));
        return distance;
    }

    /**
     * Set odometer.
     *
     * @param odometer the odometer
     */
    public void setOdometer(float odometer){
        float prevOdo = getOdometer();
        float total = prevOdo + odometer;
        setFloatProperty(Constants.ODOMETER, total);
    }

    /**
     * Get odometer float.
     *
     * @return the float
     */
    public float getOdometer(){
        return getFloatProperty(Constants.ODOMETER);
    }

    /**
     * Set distance point.
     *
     * @param distancePoint the distance point
     */
    public void setDistancePoint(float distancePoint){
        setFloatProperty(Constants.DISTANCE_POINT, distancePoint);
    }

    /**
     * Get distance point float.
     *
     * @return the float
     */
    public float getDistancePoint(){
        return getFloatProperty(Constants.DISTANCE_POINT);
    }

    private void setDumpDistance(float dumpDistance){
        float prevDumpDistance = getDumpDistance();
        float total = prevDumpDistance + dumpDistance;
        setFloatProperty(Constants.DUMP_DISTANCE, total);
    }

    /**
     * Get dump distance float.
     *
     * @return the float
     */
    public float getDumpDistance(){ return getFloatProperty(Constants.DUMP_DISTANCE);}

    /**
     * Reset dump distance.
     */
    public void resetDumpDistance(){ setFloatProperty(Constants.DUMP_DISTANCE, 0); }

    /**
     * function to update variable distance
     * used for calculate distance of sales/logistic
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void updateDistanceLocation(double lat, double lng){
        if(getLongProperty(Constants.LAST_LAT_DISTANCE)!=0){
            double[] prevCoordinate = getDistanceLastLocation();

            if(Constants.DEBUG){
                System.out.println("Prev Latitude " + prevCoordinate[0] );
                System.out.println("Prev Longitude " + prevCoordinate[1] );

                System.out.println("Current Latitude " + lat );
                System.out.println("Current Longitude " + lng );
            }


            DistanceCalculator distanceCalculator = new DistanceCalculator();
            double range = distanceCalculator.greatCircleInMeters(new LatLng(lat,lng),new LatLng(prevCoordinate[0], prevCoordinate[1]));
            if(range > 5){ // moving at 5 meters
                setOdometer((float)range);
                setDumpDistance((float)range);
                setDistanceLastLocation(lat, lng);

                if(Constants.DEBUG){
                    System.out.println("moving...");
                    System.out.println("Update Odometer add distance : " + range + " meters");
                    double lastOdometer = getOdometer();
                    System.out.println("Last Odometer : " + getOdometer());

                    double[] newCoordinate = getDistanceLastLocation();
                    System.out.println("New Latitude " + newCoordinate[0] );
                    System.out.println("New Longitude " + newCoordinate[1] );
                }
            }
        }
        else {
            setDistanceLastLocation(lat, lng);
            setOdometer(0);
            setDistancePoint(0);
        }

    }

    /**
     * function to set total distance like odometer
     *
     * @param lat the lat
     * @param lng the lng
     */
    public void calculateDistancePoint(double lat, double lng){
        if(getDumpDistance()==0){
            // if distance between location of customer < 5 meters (variable dumpDistance not update yet)
            double[] prevCoordinate = getDistanceLastLocation();
            DistanceCalculator distanceCalculator = new DistanceCalculator();
            double range = distanceCalculator.greatCircleInMeters(new LatLng(lat,lng),new LatLng(prevCoordinate[0], prevCoordinate[1]));
            range = Double.isNaN(range) ? 0 : range;
            setDistancePoint((float)range);
        } else {
            setDistancePoint(getDumpDistance());
        }
        resetDumpDistance();

        if(Constants.DEBUG){
            System.out.println("Calculate Distance Point");
            System.out.println("Distance Point : " + getDistancePoint());
        }
    }

}

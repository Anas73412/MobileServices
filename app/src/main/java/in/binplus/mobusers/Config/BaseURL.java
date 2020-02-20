package in.binplus.mobusers.Config;

public class BaseURL {

    //Store Selection

    public static final String KEY_STORE_COUNT = "STORE_COUNT";
    public static final String KEY_NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

    //Firebase
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final String KEY_PASSWORD = "password";

    //City and Store Id
    public static final String CITY_ID = "CITY_ID";
    public static final String STORE_ID = "STORE_ID";


    public static String BASE_URL = "https://mobileservices.anshuwap.com/admin/";
    public static String IMG_SLIDER_URL = BASE_URL + "uploads/sliders/";
    public static String LOGIN_URL = BASE_URL + "index.php/api/user_login";
    public static String REGISTER_URL = BASE_URL + "index.php/api/user_signup";
    public static String EDIT_PROFILE_URL = BASE_URL + "index.php/api/update_user";
    public static String CHANGE_PASSWORD = BASE_URL + "index.php/api/change_password_users";
    public static String GET_SETTINGS = BASE_URL + "index.php/api/get_settings";
    public static String ADD_ENQUIRY = BASE_URL + "index.php/api/add_enquiry";
    public static String ACTIVATE_BARCODE_URL = BASE_URL + "index.php/api/activate_barcode";
    public static String GET_SLIDER_URL = BASE_URL + "index.php/api/get_sliders";
    public static String GET_VERSTION_DATA = BASE_URL + "index.php/api/getVersionData";
    public static String GET_BARCODE_DATA = BASE_URL + "index.php/api/get_barcode_details";
    public static String SET_ORDER = BASE_URL + "index.php/api/order";
    public static String GET_ORDERS = BASE_URL + "index.php/api/user_orders";
    public static String CANCEL_ORDER = BASE_URL + "index.php/api/cancel_order";
    public static String RENEW_BARCODE = BASE_URL + "index.php/api/renew_barcode";


}

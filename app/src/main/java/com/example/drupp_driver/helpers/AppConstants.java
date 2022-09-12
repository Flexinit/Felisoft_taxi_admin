package com.example.drupp_driver.helpers;

public interface AppConstants {

    String BASE_URL = "https://africarider.net/arul/drupp-api/api/driver/";//"https://api.qualwebs.com/drupp/api/driver/";

    String IMAGE_URL = "https://africarider.net/arul/drupp-api/storage/app/public/";//"https://api.qualwebs.com/drupp/storage/app/public/";



    public static final String U_LOG_OUT = "logout";
    public static final String U_SIGN_IN = "login";
  //  public static final String U_SIGN_UP = "sign-up" ;

    public static  final String U_SIGN_UP = "sign-up";

    public static final String U_NUMBER_VERIFICATION = "number-verification";
    String U_GET_TERM_AND_CONDITION = "get-tnc";
    public static final String U_ACCEPT_RIDE = "ride-action";
    String U_FINISH_BUS_RIDE = "finish-bus-ride";
    String U_GET_DRIVER_PROFILE = "get-driver-profile";
    String U_EDIT_DRIVER_PROFILE = "edit-driver-profile";
    public static final String U_GET_RIDE_HISTORY = "get-ride-history";
    public static final String U_ACCEPT_RIDE_POST = "ride-request-action";
    String U_UPLOAD_LICENSE = "license-image-upload";
    String U_GET_WALLET_BALANCE = "get-wallet-balance";
    String U_START_RIDE = "start-user-ride";
    String U_ON_BOARD_ACTION = "on-board-action";
    String U_GET_RIDE_INFO = "get-ride-info";
    String U_FINISH_RIDE = "finish-user-ride";
    String U_EDIT_PROFILE = "edit-driver-profile";
    String U_DRIVER_AVAILABILITY_ACTION = "driver-availability-action";
    String U_UNIQUE_EMAIL_CHECK = "unique-email-check";
    String U_CANCEL_RIDE = "cancel-user-ride";
    String U_POST_RIDE = "post-ride";
    String U_START_BUS_RIDE = "start-bus-ride";
    String U_SEND_MESSAGE = "send-message";
    String U_GET_MESSAGES = "get-messages";
    String U_GET_CANCELED_RIDES = "get-canceled-rides";
    String U_GET_COMPLETED_RIDES = "get-completed-rides";
    String U_GET_RECENT_TRANSACTION = "get-recent-transaction";
    String U_GET_RECEIVED_TRANSACTION = "received-transactions";
    public static final String POOL_RIDER_DATA = "pool_rider_data";
    public static final String U_VEHICLE_IMAGE_UPLOAD = "vehicle-image-upload";
    String DRIVER_LIVE_LOCATION = "driver-live-location";
    public static final String DRIVER_SCHEDULED_RIDES = "get-driver-posted-scheduled-rides";
    public static final String DRIVER_SCHEDULED_RIDES_EDIT = "edit-driver-posted-ride";
    public static final String USER_SCHEDULED_RIDES = "get-user-posted-scheduled-rides";
    public static final String DRIVER_TRIP_SINGLE_RIDES = "get-single-ride-history";
    public static final String GET_USER_RATING = "get-rate-review";
    public static final String GET_RECENT_RIDE = "get-recent-ride-support-driver";
    public static final String RATE_USER = "rate-review";
    String U_GET_DRIVER_DASHBOARD = "driver-dashboard";
    String GET_DRIVER_DASHBOARD_COUNT = "get-driver-dashboard-count";
    public static final String U_DRIVER_SUPPORT = "post-ride-issue";
    public static final String U_GET_RIDE_DETAILS = "get-ride-details";
    String U_CHANGE_PASSWORD = "change-password";
    String U_GET_BANKS_LIST = "get-banks-list";
    String U_VERIFY_OTP = "login/verify-otp";
    public static final String U_VERIFY_RIDE_OTP = "verify-ride-otp";
    public static final String U_GET_BUS_SCHEDULE = "get-bus-schedule";
    public static final String U_GET_ALL_NOTIFICATIONS = "get-all-notifications";
    public static final String U_GET_SINGLE_SCHEDULED_DRIVER_RIDE = "get-single-scheduled-driver-ride";
    String U_DRIVER_ARRIVED_ACTION = "driver-arrived-action";
    String U_GET_SINGLE_SCHEDULED_USER_RIDE = "get-single-scheduled-user-ride";
    String U_GET_SINGLE_NOTIFICATION = "get-single-notification-detail";
    public static final int RIDE_NOW = 1;
    public static final int RIDE_LATER = 2;
    public static final int POST_RIDE_ACCEPT = 8;
    public static final int REQUEST_ACCESS_LOCATION = 48;
    public static final String K_PROFILE_IMAGE = "profile_image";
    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 932;
    public static final String ANONYMOUS = "anonymous";
    public static final String MESSAGES = "messages";
    public static final String PHOTOS = "photos";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String K_POST_RIDE_ID = "post_ride_id";
    public static final String K_CHAT_ID = "chat_id";
    public static final String K_TOKEN = "token";
    public static final String K_DRIVER_ID = "driver_id";
    public static final String K_LAUNCH_TYPE = "launch_type";


    public static final String K_RIDER_ID = "user_id";
    public static final String K_NOTIFICATION_TYPE = "notification_type";
    public static final String K_NOTIFICATION_COUNT = "notification_count";
    public static final String K_DATA = "data";
    public static final String K_BODY = "body";
    public static final String K_NOTIFICATION_RIDE = "notification_ride";
    public static final String K_FILE = "alpha";
    String K_BUS_RIDE_ID = "bus_ride_id";
    String K_PASSENGERS = "passengers";
    public static final String K_BEARER = "bearer";
    public static final String K_SENDER_ID = "senderId";
    String K_SENDERID = "sender_id";
    String K_PHONE_NUMBER = "phone_number";
    String K_ISSUE = "issue";
    public static final String K_COUNTRY_CODE = "country_code";
    String K_AVERAGE_RATING = "average_rating";
    public static final String K_PHONE = "phone";
    public static final String K_PLATFORM = "platform";
    public static final String K_FIREBASE_TOKEN = "firebase_token";
    String K_REMAINING_SEATS = "remain_seats";
    public static final String K_STATUS = "status";
    public static final String K_DRIVER_STATUS = "driver_status";
    public static final String K_DATE = "date";
    public static final String K_S_CITY = "scity";
    public static final String K_ORIGIN = "origin";
    public static final String K_TRIP_HIS_FINAL = "trip_his_final";
    public static final String K_ID = "id";
    public static final String K_RIDE_DATE = "ride_date";
    public static final String K_NAME = "name";
    public static final String K_MESSAGE = "message";
    public static final String K_BIRTH = "birth";
    public static final String K_RIDE_OPTION = "ride_option";
    public static final String K_USER_FARE = "user_fare";
    String K_WALLET_BALANCE = "wallet_balance";
    public static final String K_USER_ID = "user_id";
    public static final String K_DESTINATION = "destination";
    public static final String K_SOURCE = "source";
    public static final String K_RIDER_DETAILS = "rider_details";
    public static final String K_D_CITY = "dcity";
    public static final String K_SOURCE_LATITUDE = "source_latitude";
    public static final String K_SOURCE_LONGITUDE = "source_longitude";
    public static final String K_DESTINATION_LATITUDE = "destination_latitude";
    public static final String K_DESTINATION_LONGITUDE = "destination_longitude";
    public static final String K_PASSENGERS_PREFERENCE = "passengers_preference";
    public static final String K_BASE_FARE = "base_fare";
    public static final String K_RIDE_TYPE = "ride_type";
    public static final String K_RIDE_ID = "ride_id";
    public static final String K_TYPE = "type";
    public static final String K_CO_RIDERS_PREFERENCE = "co-riders_preference";
    public static final String K_ONE_TIME_PASSWORD = "otp";
    public static final String K_PICK_UP_LOCATION = "pick_up_location";
    public static final String K_PREFERENCE = "preference";
    public static final String K_TYPE_OF_DRIVER = "type_of_driver";
    public static final String K_REQUEST_RIDE_ID = "request_ride_id";
    public static final String K_SHOW_DIALOG = "show_dialog";
    public static final String K_UPDATE_NOTIFICATION = "update_notification";
    public static final String K_TITLE = "title";
    public static final String K_FIRST_NAME = "first_name";
    public static final String K_LAST_NAME = "last_name";
    public static final String K_EMAIL = "email";
    public static final String K_PASSWORD = "password";
    public static final String K_CITY = "city";
    public static final String K_LICENSE_NUMBER = "license_number";
    public static final String K_VEHICLE_NUMBER = "vehicle_number";
    public static final String K_VEHICLE_NAME = "vehicle_name";
    public static final String K_IMAGES = "images";
    public static final String K_VEHICLE_TYPE_ID = "vehicle_type_id";
    public static final String K_LATITUDE = "latitude";
    public static final String K_LONGITUDE = "longitude";
    public static final String K_VEHICLE_COLOR = "vehicle_color";
    public static final String K_VEHICLE_BRAND = "vehicle_brand";
    public static final String K_VEHICLE_MODEL = "vehicle_model";
    public static final String K_VEHICLE_YEAR = "model_year";
    public static final String K_CHASSIS_NUMBER = "chassis_number";
    String K_TOTAL_RIDES = "total_rides";


    public static final String K_MIN_DATE = "min_date";
    public static final String K_MAX_DATE = "max_date";
    public static final String F_RIDE_NOTIFICATION = "notification_ride";
    public static final long TIME_STAMP_AFTER_48_HOURS = 172800000;


    int IS_IMAGE = 1;
    int IS_TEXT = 2;

    public static final String NEW_MESSAGE = "New Message";
    public static final String K_IS_TYPE = "isType";

    public static final int EXISTING_USER = 2;
    public static final int NEW_USER = 3;
    String I_CANCEL_RIDE = "cancel_ride_intent";

    int PASSENGER_BOOKED = 1;
    int PASSENGER_BOARDED = 2;
    int PASSENGER_NOT_BOARDED = 3;
    int BUS_RIDE_BOOKED = 1;
    int BUS_RIDE_STARTED = 2;
    int BUS_RIDE_FINISHED = 3;
    long DELAY_FIREBASE_LOCATION_NOTIFY = 5000;
    String K_CURRENT_PASSWORD = "current_password";
    String K_NEW_PASSWORD = "new_password";
    String K_ADDRESS = "address";
    String K_STATE="state";
    String K_AGE = "age";
    String K_DATE_OF_BIRTH = "date_of_birth";
    String K_MARITAL_STATUS = "marital_status";
    int MARRIED = 1;
    int UNMARRIED = 2;
    String K_KIN_FIRST_NAME = "kin_first_name";
    String K_KIN_LAST_NAME = "kin_last_name";
    String K_KIN_EMAIL = "kin_email";
    String K_KIN_PHONE = "kin_phone";
    String U_UPLOAD_VEHICLE_IMAGE = "vehicle-image-upload";
    String K_FILE_PATH = "file_path";
    String FRONT_IMAGE_LICENSE = "1";
    String BACK_IMAGE_LICENSE = "2";

    String FRONT_IMAGE_VEHICLE = "1";
    String BACK_IMAGE_VEHICLE = "2";
    String RIGHT_IMAGE_VEHICLE = "3";
    String LEFT_IMAGE_VEHICLE = "4";
    String INTERIOR_FRONT_IMAGE_VEHICLE = "5";
    String INTERIOR_BACK_IMAGE_VEHICLE = "6";
    String ENGINE_IMAGE_VEHICLE = "7";
    String BOOT_IMAGE_VEHICLE = "8";

    String K_LICENSE_FRONT_IMAGE = "license_front_image";
    String K_LICENSE_REAR_IMAGE = "license_rear_image";
    String K_FRONT_IMAGE_VEHICLE = "vehicle_exterior_front";
    String K_BACK_IMAGE_VEHICLE = "vehicle_exterior_back";
    String K_RIGHT_IMAGE_VEHICLE = "vehicle_exterior_right";
    String K_LEFT_IMAGE_VEHICLE = "vehicle_exterior_left";
    String K_INTERIOR_FRONT_IMAGE_VEHICLE = "vehicle_interior_front";
    String K_INTERIOR_BACK_IMAGE_VEHICLE = "vehicle_interior_back";
    String K_ENGINE_IMAGE_VEHICLE = "vehicle_engine";
    String K_BOOT_IMAGE_VEHICLE = "vehicle_boot";
    int CAR_DRIVER = 1;
    int BUS_DRIVER = 2;
    int KEKE_DRIVER = 3;
    int WITHOUT_AC = 1;
    int WITH_AC = 2;
    int BUS = 3;
    int NEAT = 1;
    int NEW = 2;
    int OLD = 3;

    String K_FUNCTIONAL_AC = "functional_ac";
    String K_VEHICLE_CONDITION = "vehicle_condition";
    String K_EXPERIENCE_YR = "experience_years";
    String K_AUTHENTICATED_LICENSE = "authenticated_license";
    String K_EXPERIENCE = "experienced";
    String K_DRIVER_TYPE = "driver_type";
    String K_BANK = "bank";
    String K_ACCOUNT_NUMBER = "account_number";
    String K_BVN = "bvn";
    String U_PROFILE_IMAGE_UPLOAD = "profile-image-upload";
    String K_REASON_FOR_NO_AC = "reason_for_no_ac";
    String K_EARNINGS = "total_earnings";
    String K_RIDES_ACCEPTED = "total_accepted_rides";
    String K_RIDES_CANCELLED = "total_canceled_rides";
    String K_KM_TRAVELLED = "total_km";
    int REQUEST_IMAGE = 100;
    String K_POSTED_BY_DRIVER = "posted_by_driver";
    String K_SIGN_UP_STATE = "sign_up_state";
    String K_VEHICLE_ID = "vehicle_id";
    String K_TERMS_AND_CONDITION = "terms_and_condition";
    String K_PRIVACY_POLICY = "privacy_policy";
    String K_RIDE_INFO_MODEL = "ride_info_model";
    String CHANNEL_ID = "1";


    String ADMIN_ID = "1";
    String ADMIN = "support";
    String K_RECEIVER_ID = "receiverId";
    String ADMIN_CHAT = "admin_chat";
    long LOCATION_UPDATE_TIME_INTERVAL = 5 * 1000; //milli-seconds
    float LOCATION_UPDATE_MIN_DISTANCE = 10;     //meters
    int REQUEST_EDIT_RIDE = 312;
    String I_CHANGE_DESTINATION = "change_destination";
    String K_TOTAL_FARE = "total_fare";
    int REQUEST_SYSTE_ALERT = 328;
    String K_OTP = "otp";
    String K_USER_INFO = "userinfo";
    String K_NOTIFICATION_ID = "notificationId";
    String K_NOTIFICATION_TITLE = "notification_title";
    String SUCCESS = "success";
    String FAILURE = "failure";
    String K_PROFILE_PICTURE = "profile_picture";
    String K_LGA ="LGA" ;
    String K_RIDE_PREFERENCE ="ride preference" ;
    String K_PREFERENCE_SINGLE_RIDE ="single ride" ;
    String K_PREFERENCE_POOL_RIDE = "pool ride";
    String K_PREFERENCE_ANY ="any" ;
    String COMPLETED_TRIPS ="completed trips" ;
    String DRIVER_DETAILS ="driver details" ;
    String CANCELLED_RIDES = "cancelled trips";
    String CURRENT_RIDE_DETAILS="current ride details";
    String K_PAYMENT_OPTION ="payment_option" ;
    String K_RIDE_INFO ="ride info" ;


    enum DriverLocationStatus {
        IDLE, VIEW_RIDE, ACCEPTED, STARTED
    }

    public interface NOTIFICATION {
        String USER_POSTED_RIDE = "1";
        String DRIVER_POSTED_RIDE = "3";
    }
    enum NotificationType {
        RIDE_LATER_OTP,
        NETWORK_ERROR,
        APP_ERROR,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        CANCEL_TRANSACTION,
        NEWS_SHOP_POPUP
    }

    interface RX_EVENT {
        int PROFILE_PIC_UPDATE = 1;
        int NAME_UPDATE = 2;
        int CITY_UPDATE=3;
    }

    interface CHAT_TYPE {
        int ADMIN_CHAT = 1;
        int RIDER_CHAT = 2;
    }

    interface RIDE_OPTION {
        String RIDE_NOW = "1";
        String RIDE_LATER = "2";
    }

    public interface PAGER_RIDE {
        int DRIVER_PROFILE = 0;
        int RIDE_ACCEPT_REJECT = 1;
    }
    interface PAYMENT_METHOD {
        int WALLET = 3;
        int CASH = 5;
        int CARD = 2;
        int NET_BANKING = 4;
    }


    public static class FIREBASE_DATABASE {
        public static final String USERS = "users";
        public static final String DRIVERS = "drivers";
        public static final String RIDE_INFO = "ride_info";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LATITUDE = "latitude";
        public static final String C_LATITUDE = "cLatitude";
        public static final String C_LONGITTUDE = "cLongitude";
        public static final String LONGITUDE = "longitude";
        public static final String AVAILABILITY = "availability";
    }

    interface SIGN_UP_STATUS {
        int PERSONAL_INFO = 1;
        int NEXT_OF_KIN = 2;
        int DRIVER_EXPERIENCE = 3;
        int VEHICLE_INFO = 4;
        int ACCOUNT_INFO = 5;
        int FINISHED = -1;
    }

    interface RIDE_STATUS {
        int RIDE_POSTED = 1;
        int RIDE_ACCEPTED = 2;
        int RIDE_STARTED = 3;
        int RIDE_COMPLETED = 4;
        int RIDE_CANCELLED = 5;
        int RIDE_PAID = 6;
        int RIDE_CHANGED = 7; //Destination changed
    }

    enum SIGNUP_STEP {
        PERSONAL_INFO, NEXT_OF_KIN, EXPERIENCE, VEHICLE_TO_REGISTER, BANK_DETAILS;
    }

    int SINGLE_RIDE = 1;
    int POOL_RIDE = 2;

    interface RIDE_TYPE {
        int USER_RIDE = 1;
        int DRIVER_RIDE = 2;
    }

    String K_LOCATION_TYPE = "location_type";
    String SEARCH_DIALOG_TITLE = "search_dialog_title";
    int HOME_LOCATION = 1;
    int WORK_LOCATION = 2;

}



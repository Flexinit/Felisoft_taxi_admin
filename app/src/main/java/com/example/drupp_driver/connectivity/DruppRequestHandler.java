package com.example.drupp_driver.connectivity;

import com.example.drupp_driver.Models.AccessToken;
import com.example.drupp_driver.Models.BankDetailsModel;
import com.example.drupp_driver.Models.BusInfoModel;
import com.example.drupp_driver.Models.ChatUserMessage;
import com.example.drupp_driver.Models.DashboardCountModel;
import com.example.drupp_driver.Models.DashboardModel;
import com.example.drupp_driver.Models.DriverProfileModel;
import com.example.drupp_driver.Models.EditRideDetails;
import com.example.drupp_driver.Models.FinishFares;
import com.example.drupp_driver.Models.LoginResponse;
import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.Models.NotificationModel;
import com.example.drupp_driver.Models.PassengerInfoModel;
import com.example.drupp_driver.Models.PostRideDetails;
import com.example.drupp_driver.Models.RatingModel;
import com.example.drupp_driver.Models.RideCancelDetails;
import com.example.drupp_driver.Models.RideInfo;
import com.example.drupp_driver.Models.RideInfoModel;
import com.example.drupp_driver.Models.ScheduleRideDriverResponse;
import com.example.drupp_driver.Models.ScheduledRideUserReponse;
import com.example.drupp_driver.Models.SingleNotification;
import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.Models.UserRatingModel;
import com.example.drupp_driver.Models.WalletTransaction;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.connectivity.http.RestClient;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class DruppRequestHandler<T> extends BaseModelHandler {

    private static DruppRequestHandler druppRequestHandler;

    public static DruppRequestHandler getInstance(INetwork iNetwork) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork);
        }

        druppRequestHandler.iNetwork = iNetwork;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(INetworkList iNetwork) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork);
        }

        druppRequestHandler.iNetworkList = iNetwork;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(INetwork iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork, accessToken);
        }

        druppRequestHandler.iNetwork = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(INetworkList iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork, accessToken);
        }

        druppRequestHandler.iNetworkList = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static void clearInstance() {
        druppRequestHandler = null;
    }

    private DruppRequestHandler(INetwork iNetwork) {
        this.iNetwork = iNetwork;
    }

    private DruppRequestHandler(INetwork iNetwork, AccessToken accessToken) {
        this.iNetwork = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkList iNetwork, AccessToken accessToken) {
        this.iNetworkList = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkList iNetwork) {
        this.iNetworkList = iNetwork;
    }


    public void userSignin(HashMap<String, String> driverregistration) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get().getApiService().userSignin(driverregistration);
        call.enqueue(actionOnResponseCallBack());
    }

    public void numberVerification(HashMap<String, String> parse) {
        Call<QualStandardResponse<LoginResponse>> call = RestClient.get().getApiService().numberVerification(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getTrips() {
        Call<QualStandardResponseList<Trip>> call = RestClient.get(accessToken).getApiService().getTrips();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getSingleTripHistory(int rideId) {
        Call<QualStandardResponse<SingleRideModel>> call = RestClient.get().getApiService().getTripHistorySingle(rideId);
        call.enqueue(actionOnResponseCallBack());

    }

    public void getUserRating(String auth, int ride_id) {
        Call<QualStandardResponse<UserRatingModel>> call = RestClient.get().getApiService().getUserRating(auth, ride_id);
        call.enqueue(actionOnResponseCallBack());

    }

    public void getScheduledRidesDriver() {
        Call<QualStandardResponseList<ScheduleRideDriverResponse>> call = RestClient.get(accessToken).getApiService().getScheduledRidesDriver();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getCompletedRides() {
        Call<QualStandardResponseList<Trip>> call = RestClient.get(accessToken).getApiService().getCompletedRides();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getCanceledRides() {
        Call<QualStandardResponseList<Trip>> call = RestClient.get(accessToken).getApiService().getCanceledRides();
        call.enqueue(actionOnResponseListCallBack());

    }


    public void getScheduledRidesUser() {
        Call<QualStandardResponseList<ScheduledRideUserReponse>> call = RestClient.get(accessToken).getApiService().getScheduledRidesUser();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getSingleScheduledUserRide(int rideId) {

        Call<QualStandardResponse<SingleRideModel>> call = RestClient.get(accessToken).getApiService().getSingleScheduledUserRide(rideId);
        call.enqueue(actionOnResponseCallBack());

    }

    public void getAllNotifications() {
        Call<QualStandardResponseList<NotificationModel>> call = RestClient.get(accessToken).getApiService().getAllNotifications();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getBusSchedule() {
        Call<QualStandardResponseList<BusInfoModel>> call = RestClient.get(accessToken).getApiService().getBusSchedule();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getDashboard() {
        Call<QualStandardResponse<DashboardModel>> call = RestClient.get(accessToken).getApiService().getDashboard();
        call.enqueue(actionOnResponseCallBack());

    }
    public void getDashboardCount(String type) {
        Call<QualStandardResponse<DashboardCountModel>> call = RestClient.get(accessToken).getApiService().getDashboardCount(type);
        call.enqueue(actionOnResponseCallBack());

    }

    public void getWalletTransaction() {
        Call<QualStandardResponseList<WalletTransaction>> call = RestClient.get(accessToken).getApiService().getWalletTransaction();
        call.enqueue(actionOnResponseListCallBack());

    }


    public void getSingleScheduledDriverRide(int rideId) {
        Call<QualStandardResponse<SingleRideModel>> call = RestClient.get(accessToken).getApiService().getSingleScheduledDriverRide(rideId);
        call.enqueue(actionOnResponseCallBack());

    }


    public void getSingleNotification(int id) {
        Call<QualStandardResponse<SingleNotification>> call = RestClient.get(accessToken).getApiService().getSingleNotification(id);
        call.enqueue(actionOnResponseCallBack());

    }


    public void getDriverProfile() {
        Call<QualStandardResponse<DriverProfileModel>> call = RestClient.get(accessToken).getApiService().getDriverProfile();
        call.enqueue(actionOnResponseCallBack());

    }

    public void driverAcceptRide(HashMap<String, Integer> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverAcceptRide(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void validateEmail(String email) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().validateEmail(email);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void changePassword(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().changePassword(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void passengerBoardAction(HashMap<String, List<PassengerInfoModel>> param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().passengerBoardAction(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void driverAcceptRidePost(HashMap<String, Integer> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverAcceptRidePost(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getTermsAndCondition() {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().getTermsAndCondition();
        call.enqueue(actionOnResponseCallBack());
    }

    public void driverStartRide(HashMap<String, Integer> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverStartRide(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void driverFinishRide(HashMap<String, Integer> parse) {
        Call<QualStandardResponse<FinishFares>> call = RestClient.get(accessToken).getApiService().driverFinishRide(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void managerDriverStatus(HashMap<String, Integer> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverStatus(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void driverCancelRide(RideCancelDetails rideCancelDetails) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverCancelRide(rideCancelDetails);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void postRide(PostRideDetails postRideDetails) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().postRide(postRideDetails);
        call.enqueue(actionOnResponseCallBack());
    }

    public void finishBusRide(HashMap<String, String> param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().finishBusRide(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void editRide(String auth, EditRideDetails editRideDetails) {
        Call<QualStandardResponseList<Token>> call = RestClient.get().getApiService().editRide(auth, editRideDetails);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void editDriverProfile(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get().getApiService().editDriverProfile(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void rateUser(String auth, RatingModel ratingModel) {
        Call<QualStandardResponseList<Token>> call = RestClient.get().getApiService().rateUser(auth, ratingModel);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void startRide(HashMap<String, String> param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().startBusRide(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void sendMessage(ChatUserMessage chatUserMessage) {
        Call<QualStandardResponse<ChatUserMessage>> call = RestClient.get(accessToken).getApiService().sendMessage(chatUserMessage);
        call.enqueue(actionOnResponseCallBack());
    }

    public void driverLiveLocation(HashMap<String, Double> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().driverLiveLocstion(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void updateUserImage(String filePath) {
        File file;
        try {
            file = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        RequestBody photo = RequestBody.create(MediaType.parse("application/image"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), photo)
                .build();

        Call<QualStandardResponse<MyResponse>> call = RestClient.get(accessToken).getApiService().updateUserProfile(body);
        call.enqueue(actionOnResponseCallBack());
    }

    public void uploadLicenseImage(String filePath, String imageType) {
        File file;
        try {
            file = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        RequestBody photo = RequestBody.create(MediaType.parse("application/image"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), photo)
                .addFormDataPart("type", imageType)
                .build();

        Call<QualStandardResponse<MyResponse>> call = RestClient.get(accessToken).getApiService().updateLicenseImage(body);
        call.enqueue(actionOnResponseCallBack());
    }

    public void uploadVehicleImage(String filePath, String fileType) {
        File file;
        try {
            file = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        RequestBody photo = RequestBody.create(MediaType.parse("application/image"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), photo)
                .addFormDataPart("type", fileType)
                .build();

        Call<QualStandardResponse<MyResponse>> call = RestClient.get(accessToken).getApiService().uploadVehicleImage(body);
        call.enqueue(actionOnResponseCallBack());
    }

    public void logOut(HashMap<String, String> params) {
        Call<QualStandardResponseList<Token>> call = RestClient.get(accessToken).getApiService().logOut(params);
        call.enqueue(actionOnResponseListCallBack());
    }


    public void getRecentRide() {
        Call<QualStandardResponse<Trip>> call = RestClient.get(accessToken).getApiService().getRecentRide();
        call.enqueue(actionOnResponseCallBack());

    }

    public void getBanks() {
        Call<QualStandardResponseList<BankDetailsModel>> call = RestClient.get(accessToken).getApiService().getBanksList();
        call.enqueue(actionOnResponseListCallBack());

    }

    public void getRideInfo(int isDriverPosted, int rideId) {
        Call<QualStandardResponse<RideInfoModel>> call = RestClient.get(accessToken).getApiService().getRideInfo(isDriverPosted, rideId);
        call.enqueue(actionOnResponseCallBack());

    }

    public void driverSupport(String auth, HashMap<String, String> parse) {
        Call<QualStandardResponseList<Token>> call = RestClient.get().getApiService().driverSupport(auth, parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRideDetails(HashMap<String, String> parse) {
        Call<QualStandardResponse<RideInfo>> call = RestClient.get(accessToken).getApiService().getRideDetails(parse);
        call.enqueue(actionOnResponseCallBack());
    }


    public void getWalletBalance() {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().getWalletBalance();
        call.enqueue(actionOnResponseCallBack());
    }

    public void codeVerification(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().codeVerification(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void verifyOtp(HashMap<String, Object> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().verifyOtp(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void driverArrivedAction(HashMap<String, Object> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().driverArrivedAction(params);
        call.enqueue(actionOnResponseListCallBack());
    }

}
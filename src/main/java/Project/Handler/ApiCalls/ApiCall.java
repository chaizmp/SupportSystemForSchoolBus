package Project.Handler.ApiCalls;

import Project.Model.Notification.NotificationMessage;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 23/2/2560.
 */
@Service
public class ApiCall {

    public FireBaseService fireBaseService;

    interface FireBaseService {
        @Headers({
                "Authorizationt: application/vnd.github.v3.full+json",
                "Content-Type: application/json"
        })
        @POST("/fcm/send")
        Call<List<String>> sendNotification(@Body NotificationMessage notificationMessage);
    }

    public ApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fireBaseService = retrofit.create(FireBaseService.class);
    }

    public String sendGetOnOrOffNotificationToPersons(NotificationMessage notificationMessage) {
        Call<List<String>> call = fireBaseService.sendNotification(notificationMessage);
        try {
            Response<List<String>> response = call.execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().get(0));
                return response.body().get(0);
            } else {
                System.out.println("cannot get sale order : " + response.errorBody().string());
            }
        } catch (IOException e) {
            System.out.println("cannot get sale order : " + e.getLocalizedMessage());
        }
        return null;
    }

    public boolean homeArrivalNotification() {
        return true;
    }

    public boolean schoolArrivalNotification() {
        return true;
    }

    public boolean broadcastNotification() {
        return true;
    }

    public boolean otherPersonNotification() {
        return true;
    }

    public boolean alarmNotification() {
        return true;
    }
}

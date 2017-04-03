package Project.Handler.ApiCalls;

import Project.Model.Notification.NotificationMessage;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    public CameraService cameraService;

    interface FireBaseService {
        @Headers({
                "Authorization: key=AAAAtg0JKE8:APA91bHzdV7qCSl35IswRuPhDbwEkTamirIJgjLsUdj5XnvZeGh--3Ex2fQU96JrHzVUmJEamZJMf1a72dDiNJXU8tgjIXIYqNyqMoQ1yJ4mAQFAlKSyFFCGwkV9i_psUWq_nyiCl7YR",
                "Content-Type: application/json"
        })
        @POST("/fcm/send")
        Call<JsonObject> sendNotification(@Body NotificationMessage notificationMessage);
    }

    interface CameraService {
        @GET("/snapshot.cgi?user=admin&pwd=sakchailab606")
        Call<JsonObject> getSnapshot();
    }

    public ApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fireBaseService = retrofit.create(FireBaseService.class);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11:81/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cameraService = retrofit2.create(CameraService.class);

    }

    public void sendGetOnOrOffNotificationToPersons(NotificationMessage notificationMessage) {
        Call<JsonObject> call = fireBaseService.sendNotification(notificationMessage);
        try {
            Response<JsonObject> response = call.execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().toString());
               // return response.body().toString();
            } else {
                System.out.println(response.errorBody().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        //return null;
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

    public String getPicFromCamera() {
        Call<JsonObject> call = cameraService.getSnapshot();
        try {
            Response<JsonObject> response = call.execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().toString());
                return response.body().toString();
            } else {
                System.out.println(response.errorBody().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }
}

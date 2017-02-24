package Project.Model.Notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by User on 23/2/2560.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationMessage {
    @JsonProperty("notification")
    NotificationForm notificationForm;
    @JsonProperty("to")
    String to;


}

package Project.Model.Notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by User on 23/2/2560.
 */
@Data
@JsonIgnoreProperties
public class NotificationForm {
    String title;
    String body;
}

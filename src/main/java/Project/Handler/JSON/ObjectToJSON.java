package Project.Handler.JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by User on 3/1/2560.
 */
@Service
public class ObjectToJSON {
    public JSONObject mapToJSON(String objName, Object object) {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject result = new JSONObject();
        if (object != null) {
            try {
                String jsonString = mapper.writeValueAsString(object);
                JSONObject jsonTarget = new JSONObject(jsonString);
                return result.put(objName, jsonTarget);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public <T> JSONObject arrayListToJSON(String listName, ArrayList<T> objects) {
        ObjectMapper mapper = new ObjectMapper(); // always put a jsonObject as the parameter in JSONArray, JSONObject
        JSONArray objectJSONArray = new JSONArray();
        if (objects != null && objects.size() != 0) {
            try {
                for (T it : objects) {
                    objectJSONArray.put(new JSONObject(mapper.writeValueAsString(it)));
                }
                JSONObject jsonObject = new JSONObject();
                return jsonObject.put(listName, objectJSONArray);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public JSONObject mergeJSONObjects(JSONObject... jsonObjects) {
        JSONObject jsonObjectResult = new JSONObject();
        for (JSONObject jsonObject : jsonObjects) {
            if (jsonObject != null) {
                for (String fieldName : JSONObject.getNames(jsonObject)) {
                    jsonObjectResult.put(fieldName, jsonObject.get(fieldName));
                }
            }
        }
        return jsonObjectResult;
    }
}


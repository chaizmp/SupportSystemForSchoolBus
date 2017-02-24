package Project.Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    //public static void main(String[] args) throws Exception {

//        HttpURLConnectionExampleAD http = new HttpURLConnectionExampleAD();

    // System.out.println("Testing 1 - Send Http GET request");
    //http.sendGet();

    //System.out.println("\nTesting 2 - Send Http POST request");
    //http.sendPost();
    //ArrayList<String> stList = new ArrayList<String>();
    //stList.add("123");
    //stList.add("456");
    //stList.add("777");
    // stList.add("888");
    // stList.add("999");
    // System.out.println(stList.toString());
    //CompStory.CompLocationNameUpperLower x = new CompStory.CompLocationNameUpperLower();
    // x.setComp("KMITL");
    /*  System.out.println(x.compareSize("KMITL","kmitl"));
        System.out.println(x.compareSize("kmitL","KMITL"));
        System.out.println(x.compareSize("kmiTl","KMITL"));
        System.out.println(x.compareSize("kmiTL","KMITL"));
        System.out.println(x.compareSize("kmItl","KMITL"));
        System.out.println(x.compareSize("kmItL","KMITL"));
        System.out.println(x.compareSize("kmITl","KMITL"));
        System.out.println(x.compareSize("kmITL","KMITL"));
        System.out.println(x.compareSize("kMitl","KMITL"));
        System.out.println(x.compareSize("kMitL","KMITL"));
        System.out.println(x.compareSize("kMiTl","KMITL"));
        System.out.println(x.compareSize("kMiTL","KMITL"));
        System.out.println(x.compareSize("kMItl","KMITL"));
        System.out.println(x.compareSize("kMItL","KMITL"));
        System.out.println(x.compareSize("kMITl","KMITL"));
        System.out.println(x.compareSize("kMITL","KMITL"));
        System.out.println(x.compareSize("Kmitl","KMITL"));
        System.out.println(x.compareSize("KmitL","KMITL"));
        System.out.println(x.compareSize("KmiTl","KMITL"));
        System.out.println(x.compareSize("KmiTL","KMITL"));
        System.out.println(x.compareSize("KmItl","KMITL"));
        System.out.println(x.compareSize("KmItL","KMITL"));
        System.out.println(x.compareSize("KmITl","KMITL"));
        System.out.println(x.compareSize("KmITL","KMITL"));
        System.out.println(x.compareSize("KMitl","KMITL"));
        System.out.println(x.compareSize("KMitL","KMITL"));
        System.out.println(x.compareSize("KMiTl","KMITL"));
        System.out.println(x.compareSize("KMiTL","KMITL"));
        System.out.println(x.compareSize("KMItl","KMITL"));
        System.out.println(x.compareSize("KMItL","KMITL"));
        System.out.println(x.compareSize("KMITl","KMITL"));
        System.out.println(x.compareSize("KMITL","kMITL"));*/


    //  System.out.println(h.stringToArray(stList.toString()).toString());
    //}
    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=mkyong";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    public void sendPost(String param) throws Exception {
  /*  String urlParameters = "privacy=1&" +
                "des=testNEWSTORY&" +
                "fbid=1021181027942407&" +
                "locationId=0"+"&"+
                "locationName="+param+"&"+
                "address=LAD KRABANG1234&" +
                "img=[]";*/
        String urlParameters = "fbid=10208073652868812";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = "http://203.151.92.173:8080/notification";
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();
        //print result
        System.out.println(response.toString());

    }

}


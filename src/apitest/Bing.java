package apitest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
public class Bing {
    JSONArray jsonarr;
    ArrayList<Result> results;
    public void search(String query, int howMany){
        results=new ArrayList();
        String searchText = query;
        searchText = searchText.replaceAll(" ", "%20");
        String accountKey="FdqqdsirWlV5K7MKTy1j9KAPPRTJyPXFFNOGGe3lFBk";
        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        URL url;
        try {
            url = new URL("https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27"+searchText+"%27&$top="+howMany+"&$format=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        char[] buffer = new char[4096];
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();
        JSONObject json = new JSONObject(sb.toString());
        json=json.getJSONObject("d");
        jsonarr= json.getJSONArray("results");
        setResults();
        } catch (MalformedURLException e1) {
        } catch (IOException e) {
        }
    }
    public void setResults(){
        for(int i=0;i<jsonarr.length();i++){
            results.add(new Result());
            results.get(i).setTitle(jsonarr.getJSONObject(i).getString("Title"));
            results.get(i).setURL(jsonarr.getJSONObject(i).getString("Url"));
        }
    }
    public ArrayList<Result> getResults(){
        return results;
    }
}
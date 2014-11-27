package apitest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
public class Google {
    JSONArray jsonarr;
    ArrayList<Result> results;
    public void search(String query, int howMany) throws MalformedURLException, IOException{
        results=new ArrayList();
        query=query.replace(" ", "%20");
        URL url = new URL("https://ajax.googleapis.com/ajax/services/search/web?" +
                  "v=1.0&q="+query+"&rsz="+howMany);
        URLConnection connection = url.openConnection();
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }   
        JSONObject json = new JSONObject(builder.toString());
        json=json.getJSONObject("responseData");
        jsonarr= json.getJSONArray("results");
        setResults();
    }
    public void setResults(){
        for(int i=0;i<jsonarr.length();i++){
            results.add(new Result());
            results.get(i).setTitle(jsonarr.getJSONObject(i).getString("titleNoFormatting"));
            results.get(i).setURL(jsonarr.getJSONObject(i).getString("url"));
        }
    }
    public ArrayList<Result> getResults(){
        return results;
    }
}
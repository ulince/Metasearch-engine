package apitest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
public class APITest {
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        //Google Search
        Google google=new Google();
        google.search("barack obama", 8);
        ArrayList<Result> googleResults=google.getResults();
        for(int i=0;i<googleResults.size();i++)
            System.out.println(googleResults.get(i)+"\n");
        //Bing Search
        Bing bing=new Bing();
        bing.search("barack obama", 8);
        ArrayList<Result> bingResults=bing.getResults();
        for(int i=0;i<bingResults.size();i++)
            System.out.println(bingResults.get(i)+"\n");
    }
}
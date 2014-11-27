package apitest;
public class Result {
    String title;
    String URL;
    public String getTitle() {
        return title;
    }
    public String getURL() {
        return URL;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }
    @Override
    public String toString() { 
        return "Title: "+title+"\nURL: "+URL;
    }
}
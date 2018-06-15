import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/14.
 */
public class HttpClientTest {
    public static void main(String[] args) {
        CloseableHttpClient client =  HttpClients.custom().build();


        HttpGet get = new HttpGet("http://localhost:8080");
        try {
            CloseableHttpResponse response = client.execute(get);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

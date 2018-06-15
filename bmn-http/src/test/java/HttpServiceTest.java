import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.*;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/6.
 */
public class HttpServiceTest {
    public static void main(String[] args) {

    }

    public static void server() {
        HttpRequestHandler handler = new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

            }
        };

        HttpService httpService;
        HttpRequestHandlerMapper mapper = new HttpRequestHandlerMapper() {
            @Override
            public HttpRequestHandler lookup(HttpRequest request) {
                return null;
            }
        };

    }
}

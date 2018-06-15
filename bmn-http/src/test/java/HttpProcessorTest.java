import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.*;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/5.
 */
public class HttpProcessorTest {

    public static void main(String[] args) throws IOException, HttpException {
        processor();
    }

    public static void processor() throws IOException, HttpException {
        HttpProcessor httpProc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("my agenet"))
                .add(new RequestExpectContinue(true))
                .build();

        HttpCoreContext context = HttpCoreContext.create();
        HttpRequest request = new BasicHttpRequest("get", "/");
        httpProc.process(request, context);
    }
}

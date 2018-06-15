import org.apache.http.*;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/1.
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        requestRequestLine();
        logger.debug("start response...");
        responseStatusLine();


        responseHeaders();
    }

    private static void requestRequestLine() {
        HttpRequest request = buildRequest();

        RequestLine line = request.getRequestLine();

        ProtocolVersion version = line.getProtocolVersion();
        logger.debug(line.getMethod());
        logger.debug(line.getUri());
        logger.debug(version.toString());
        logger.debug(version.getProtocol());
        logger.debug(line.toString());
    }

    private static void responseStatusLine() {
        HttpResponse response = buildResponse();

        StatusLine line = response.getStatusLine();
        ProtocolVersion version = line.getProtocolVersion();

        logger.debug(version.toString());
        logger.debug(line.getStatusCode() +"");
        logger.debug(line.getReasonPhrase());
        logger.debug(line.toString());
    }


    private static void responseHeaders() {
        logger.debug("start printing response headers");

        HttpResponse response = buildResponse();
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        logger.debug(h1.toString());
        Header h2 = response.getLastHeader("Set-Cookie");
        logger.debug(h2.toString());
        Header[] hs = response.getHeaders("Set-Cookie");
        logger.debug(hs.length +"");

        HeaderIterator iterator = response.headerIterator("Set-Cookie");
        while (iterator.hasNext()) {
            logger.debug(iterator.next().toString());
        }


        HeaderElementIterator elementIterator = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
        while (elementIterator.hasNext()) {
            HeaderElement element = elementIterator.nextElement();
            logger.debug("{}-{}", element.getName(), element.getValue());

            NameValuePair[] params = element.getParameters();
            for (NameValuePair param : params) {
                logger.debug("param: {}", param.toString());
            }

        }
    }

    private static HttpRequest buildRequest() {
        return new BasicHttpRequest("GET", "/", HttpVersion.HTTP_1_1);
    }

    private static HttpResponse buildResponse() {
        return new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
    }
}

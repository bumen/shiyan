import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.protocol.HttpCoreContext;

/**
 * Created by Administrator on 2017/4/14.
 */
public class RoutePlannerTester {

    public static void main(String[] args) {
        SystemDefaultRoutePlanner planner = new SystemDefaultRoutePlanner(null);

        try {
            planner.determineRoute(new HttpHost("127.0.0.1", -1, "http"),
                    new BasicHttpRequest(new BasicRequestLine("get", "/a.jsp", HttpVersion.HTTP_1_1)),
                    new HttpCoreContext());
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
}

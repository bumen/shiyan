import org.apache.http.HttpVersion;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.BasicRequestLine;

/**
 * Created by Administrator on 2017/4/13.
 */
public class LineFormatterTest {
    public static void main(String[] args) {

        String s = BasicLineFormatter.formatRequestLine(new BasicRequestLine("get", "/a.jsp", HttpVersion.HTTP_1_1),null);
        System.out.println(s);
    }

}

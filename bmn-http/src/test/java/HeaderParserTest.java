import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BufferedHeader;

/**
 * Created by Administrator on 2017/4/13.
 */
public class HeaderParserTest {
    public static void main(String[] args) {

        String s = "Set-Cookie:__bsi=12038844680303_C02F_N_N_Y_0; expires=Thu, 13-Apr-17 03:26:22 GMT; domain=www.baidu.com; path=/";

        Header header = BasicLineParser.parseHeader(s, null);


        String value = header.getValue();

        HeaderElement[] elements = header.getElements();







    }
}

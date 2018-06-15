import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/5.
 */
public class EnterTest {

    public static void main(String[] args) throws IOException {
        EnterTest t = new EnterTest();
        t.stringEntry();
    }

    public void stringEntry() throws IOException {
        StringEntity myEntry = new StringEntity("importment message", Consts.UTF_8);

        System.out.println(myEntry.getContentType());
        System.out.println(myEntry.getContentLength());
        System.out.println(EntityUtils.toString(myEntry));
        System.out.println(EntityUtils.toByteArray(myEntry).length);
    }

    public void closeEntry() throws IOException {
        HttpResponse response = null;
        HttpEntity entry = response.getEntity();
        if(entry != null) {
            InputStream is = entry.getContent();
            try {
                //do
            } finally {
                is.close();
            }
        }
    }

    public void entry() {
        BasicHttpEntity basicHttpEntity;
    }


}

import java.nio.channels.spi.SelectorProvider;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Enumeration;
import java.util.Set;
import sun.misc.BASE64Encoder;

public class Test {

    public static void main(String[] args) {
        Provider provider;
        SelectorProvider selectorProvider;

        System.out.println(Security.getProperty("security.provider.1"));

        Key  key;

        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            //System.out.println(p);

            Enumeration<Object> keys = p.keys();
            //while(keys.hasMoreElements()) {
                //System.out.println(keys.nextElement());
           // }

            Set<Service> serviceSet = p.getServices();
            for(Service s : serviceSet) {
                System.out.println(s.getType() + "--" + s.getAlgorithm());
            }

            System.out.println("-----");
        }

       // MessageDigest messageDigest = MessageDigest.getInstance("");

    }
}

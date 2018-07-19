import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestTest {

    private static void messageDisgest() throws NoSuchAlgorithmException {
        byte[] input = "md5".getBytes();
        MessageDigest m = MessageDigest.getInstance("md5");
        m.update(input);

        byte[] b =m.digest();

        System.out.println(new String(b));

    }

    private static void messageInputStream() throws NoSuchAlgorithmException, IOException {
        byte[] input = "md5".getBytes();
        MessageDigest m = MessageDigest.getInstance("md5");
        DigestInputStream is = new DigestInputStream(new ByteArrayInputStream(input), m);

        is.read(input);

        byte[] o = is.getMessageDigest().digest();

        is.close();

        System.out.println(new String(o));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        messageDisgest();

        messageInputStream();

    }

}

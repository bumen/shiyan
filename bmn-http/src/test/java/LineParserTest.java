import org.apache.http.*;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/**
 * Created by Administrator on 2017/4/13.
 */
public class LineParserTest {

    public static void main(String[] args) {
        String requestLineStr = "  GET /a.jsp HTTP/1.1 ";

        RequestLine requestLine = TLineParser.parseRequestLine(requestLineStr, TLineParser.INSTANCE);

        System.out.println(requestLine.toString());

        System.out.println("parse ok");
    }

    private static class TLineParser implements LineParser{
        public final static TLineParser INSTANCE = new TLineParser();

        public static
        RequestLine parseRequestLine(final String value,
                                     final LineParser parser) throws ParseException {
            Args.notNull(value, "Value");

            final CharArrayBuffer buffer = new CharArrayBuffer(value.length());
            buffer.append(value);
            final ParserCursor cursor = new ParserCursor(0, value.length());
            return (parser != null ? parser : TLineParser.INSTANCE)
                    .parseRequestLine(buffer, cursor);
        }

        public RequestLine parseRequestLine(final CharArrayBuffer buffer,
                                            final ParserCursor cursor) throws ParseException {

            Args.notNull(buffer, "Char array buffer");
            Args.notNull(cursor, "Parser cursor");
            final int indexFrom = cursor.getPos();
            final int indexTo = cursor.getUpperBound();

            try {
                skipWhitespace(buffer, cursor);
                int i = cursor.getPos();

                int blank = buffer.indexOf(' ', i, indexTo);
                if (blank < 0) {
                    throw new ParseException("Invalid request line: " +
                            buffer.substring(indexFrom, indexTo));
                }
                final String method = buffer.substringTrimmed(i, blank);
                cursor.updatePos(blank);

                skipWhitespace(buffer, cursor);
                i = cursor.getPos();

                blank = buffer.indexOf(' ', i, indexTo);
                if (blank < 0) {
                    throw new ParseException("Invalid request line: " +
                            buffer.substring(indexFrom, indexTo));
                }
                final String uri = buffer.substringTrimmed(i, blank);
                cursor.updatePos(blank);

                final ProtocolVersion ver = parseProtocolVersion(buffer, cursor);

                skipWhitespace(buffer, cursor);
                if (!cursor.atEnd()) {
                    throw new ParseException("Invalid request line: " +
                            buffer.substring(indexFrom, indexTo));
                }

                return createRequestLine(method, uri, ver);
            } catch (final IndexOutOfBoundsException e) {
                throw new ParseException("Invalid request line: " +
                        buffer.substring(indexFrom, indexTo));
            }
        }

        @Override
        public StatusLine parseStatusLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
            return null;
        }

        @Override
        public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
            return null;
        }

        protected RequestLine createRequestLine(final String method,
                                                final String uri,
                                                final ProtocolVersion ver) {
            return new BasicRequestLine(method, uri, ver);
        }

        public ProtocolVersion parseProtocolVersion(final CharArrayBuffer buffer,
                                                    final ParserCursor cursor) throws ParseException {
            Args.notNull(buffer, "Char array buffer");
            Args.notNull(cursor, "Parser cursor");
            final String protoname = this.protocol.getProtocol();
            final int protolength  = protoname.length();

            final int indexFrom = cursor.getPos();
            final int indexTo = cursor.getUpperBound();

            skipWhitespace(buffer, cursor);

            int i = cursor.getPos();

            // long enough for "HTTP/1.1"?
            if (i + protolength + 4 > indexTo) {
                throw new ParseException
                        ("Not a valid protocol version: " +
                                buffer.substring(indexFrom, indexTo));
            }

            // check the protocol name and slash
            boolean ok = true;
            for (int j=0; ok && (j<protolength); j++) {
                ok = (buffer.charAt(i+j) == protoname.charAt(j));
            }
            if (ok) {
                ok = (buffer.charAt(i+protolength) == '/');
            }
            if (!ok) {
                throw new ParseException
                        ("Not a valid protocol version: " +
                                buffer.substring(indexFrom, indexTo));
            }

            i += protolength+1;

            final int period = buffer.indexOf('.', i, indexTo);
            if (period == -1) {
                throw new ParseException
                        ("Invalid protocol version number: " +
                                buffer.substring(indexFrom, indexTo));
            }
            final int major;
            try {
                major = Integer.parseInt(buffer.substringTrimmed(i, period));
            } catch (final NumberFormatException e) {
                throw new ParseException
                        ("Invalid protocol major version number: " +
                                buffer.substring(indexFrom, indexTo));
            }
            i = period + 1;

            int blank = buffer.indexOf(' ', i, indexTo);
            if (blank == -1) {
                blank = indexTo;
            }
            final int minor;
            try {
                minor = Integer.parseInt(buffer.substringTrimmed(i, blank));
            } catch (final NumberFormatException e) {
                throw new ParseException(
                        "Invalid protocol minor version number: " +
                                buffer.substring(indexFrom, indexTo));
            }

            cursor.updatePos(blank);

            return createProtocolVersion(major, minor);

        } // parseProtocolVersion

        @Override
        public boolean hasProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) {
            return false;
        }

        protected ProtocolVersion createProtocolVersion(final int major, final int minor) {
            return protocol.forVersion(major, minor);
        }

        public TLineParser(final ProtocolVersion proto) {
            this.protocol = proto != null? proto : HttpVersion.HTTP_1_1;
        }

        public TLineParser() {
            this(null);
        }
        protected final ProtocolVersion protocol;

        protected void skipWhitespace(final CharArrayBuffer buffer, final ParserCursor cursor) {
            int pos = cursor.getPos();
            final int indexTo = cursor.getUpperBound();
            while ((pos < indexTo) &&
                    HTTP.isWhitespace(buffer.charAt(pos))) {
                pos++;
            }
            cursor.updatePos(pos);
        }
    }
}

package server;

import com.bmn.http.httpclient.ServerHandler;
import com.bmn.http.httpclient.config.SocketConfig;
import com.bmn.http.httpclient.impl.server.HttpIoServer;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.*;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/24.
 */
public class BT {
    public static void main(String[] args) {


        HttpProcessor httpProcessor = null;
        if(httpProcessor == null) {
            HttpProcessorBuilder builder = HttpProcessorBuilder.create();
            builder.add(new ResponseDate());
            httpProcessor = builder.build();
        }

        HttpRequestHandlerMapper handlerMapper = null;
        if(handlerMapper == null) {
            UriHttpRequestHandlerMapper register = new UriHttpRequestHandlerMapper();
            register.register("*", new ServerHandler());

            handlerMapper = register;
        }

        ConnectionReuseStrategy connectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        HttpResponseFactory httpResponseFactory = DefaultHttpResponseFactory.INSTANCE;

        HttpService httpService = new HttpService(httpProcessor,
                connectionReuseStrategy, httpResponseFactory, handlerMapper, null);


        int port = 8080;
        HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory
                = DefaultBHttpServerConnectionFactory.INSTANCE;

        HttpIoServer server = new HttpIoServer(
                port,
                SocketConfig.custom().build(),
                httpService,
                connectionFactory);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

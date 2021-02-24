package com.bmn.springboot.client.service;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.AlibabaCloudCredentials;
import com.aliyuncs.BasicCredentials;
import com.aliyuncs.HmacSHA1Signer;
import com.aliyuncs.ISignatureComposer;
import com.aliyuncs.ParameterHelper;
import com.aliyuncs.RoaSignatureComposer;
import com.aliyuncs.Signer;
import com.bmn.springboot.client.pojo.Message;

/**
 * @date 2020-10-22
 * @author zhangyuqiang02@playcrab.com
 */
public enum AliPictureService {
    /**
     *
     */
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(AliPictureService.class);

    private HttpClient httpClient;

    private ISignatureComposer composer = new RoaSignatureComposer();

    private Signer signer = new HmacSHA1Signer();

    private AlibabaCloudCredentials acsCredentials;

    private AlibabaCloudCredentials ossCredentials;

    private String OSS_HOST;
    private String OSS_IMAGE_BUCKET;
    private String OSS_IMAGE_STYLE;
    private String OSS_IMAGE_HOST;

    private WebClient client ;

    public void init() {

        OSS_HOST = "oss-cn-hangzhou.aliyuncs.com";
        OSS_IMAGE_BUCKET = "playcrab-public-dev";
        OSS_IMAGE_HOST = "http://" + OSS_IMAGE_BUCKET + "." + OSS_HOST + "/";

        OSS_IMAGE_STYLE = "http://green.cn-beijing.aliyuncs.com";
        String accessKeyId = "123";
        String accessKeySecret = "123";

        ossCredentials = new BasicCredentials(accessKeyId, accessKeySecret);

        String host = "http://localhost:9000";
        accessKeyId = "123";
        accessKeySecret = "123";

        acsCredentials = new BasicCredentials(accessKeyId, accessKeySecret);

        ConnectionProvider pool = ConnectionProvider.fixed("Im-builder", 20, 6000);

        TcpClient tcpClient = TcpClient.create(pool) // 使用默认的配置 ConnectionProvider
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(6))
                        .addHandlerLast(new WriteTimeoutHandler(3)));

        httpClient = HttpClient.from(tcpClient).baseUrl(host).headers(builder -> {
            builder.set("Accept", "application/json");
            builder.set("Content-Type", "application/json;charset=utf-8");
            builder.set("x-acs-version", "2018-05-09");
            builder.set("x-acs-signature-method", signer.getSignerName());
            builder.set("x-acs-signature-version", signer.getSignerVersion());
            builder.set("bizType", "odin_im");
        });

        client = WebClient.create(host);

        logger.info("AliPictureService#init oss:{} image host:{} key:{} secret:{} success",
                OSS_HOST, host, accessKeyId, accessKeySecret);
    }

    /**
     * 图版消息校验
     * @param message 消息
     * @param consumer 回调
     */
    public void check(Message message, Consumer<String> consumer) {
        logger.debug("ali image check  start message:{}", message);

        String content = message.getContent();

        final String imageUrl = OSS_IMAGE_HOST + content;

        Map<String, Object> task = new LinkedHashMap<>(2);
        task.put("dataId", message.getMessageId());
        task.put("url", "http://f.hiphotos.baidu.com/image/pic/item/aa18972bd40735fa13899ac392510fb30f24084b.jpg");

        JSONObject data = new JSONObject();
        data.put("scenes", new String[]{"porn", "terrorism"});
        data.put("tasks", new Map[]{task});

        final byte[] contentBytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
        Flux<byte[]> mono = httpClient.post().uri("/green/image/scan")
                .send((request, outbound) -> {
                    HttpHeaders httpHeaders = request.requestHeaders();
                    httpHeaders.set("Date", ParameterHelper.getRFC2616Date(null));
                    httpHeaders.set("x-acs-signature-nonce", signer.getSignerNonce());

                    // 签名
                    String strMd5 = ParameterHelper.md5Sum(contentBytes);
                    httpHeaders.set("Content-MD5", strMd5);
                    httpHeaders.set("Content-Length", contentBytes.length);

                    // 加密
                    String strToSign = composer.composeStringToSign(request, signer,
                            Collections.emptyMap());

                    logger.debug("ali check picture sign str:{}", strToSign);

                    String signature = signer.signString(strToSign, acsCredentials);
                    httpHeaders.set("Authorization",
                            "acs " + acsCredentials.getAccessKeyId() + ":" + signature);

                    ByteBuf datas = outbound.alloc().buffer(contentBytes.length);
                    datas.writeBytes(contentBytes);

                    Flux<ByteBuf> byteBufFlux = Flux.just(datas);
                    Publisher<Void> xx = outbound.send(byteBufFlux).then();

                    return Flux.from(xx).then();
                })
                .responseContent()
                .asString(Charset.forName("utf-8"))
                .retry(1)
                .map(jsonStr -> {
                    JSONObject scrResponse = JSON.parseObject(jsonStr);
                    int code = scrResponse.getIntValue("code");
                    if (200 != code) {
                        logger.debug("ali check picture:{} result error code:{}",
                                message.getMessageId(), code);
                        return 100;
                    }

                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        code = ((JSONObject) taskResult).getInteger("code");
                        if (200 != code) {
                            logger.debug("ali check picture:{} task error:{}",
                                    message.getMessageId(), code);
                            return 101;
                        }

                        JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                        for (Object sceneResult : sceneResults) {
                            String suggestion = ((JSONObject) sceneResult).getString("suggestion");

                            logger.debug("ali check picture:{} suggestion:{}",
                                    message.getMessageId(), suggestion);
                            if ("pass".equals(suggestion)) {
                                return 0;
                            } else {
                                return 102;
                            }
                        }
                    }

                    return 103;

                })
                .onErrorReturn(1)
                .flatMap((resp) -> {
                    // 校验成功后，获取缩略图
                    if (resp == 0 || resp == 1) {
                        return getImage(content);
                    }

                    // 校验失败
                    return Mono.just(EMPTY_DATA);
                })
                .log(null, Level.ALL);

        mono.subscribe((resp) -> {
            logger.debug("ali picture check finish result is error:{}", resp == EMPTY_DATA);

            consumer.accept(null);
        });

    }

    public void checkTo(Message message, Consumer<String> consumer)
            throws ExecutionException, InterruptedException {
        Map<String, Object> task = new LinkedHashMap<>(2);
        task.put("dataId", message.getMessageId());
        task.put("url", "http://f.hiphotos.baidu.com/image/pic/item/aa18972bd40735fa13899ac392510fb30f24084b.jpg");

        JSONObject data = new JSONObject();
        data.put("scenes", new String[]{"porn", "terrorism"});
        data.put("tasks", new Map[]{task});

        String r = data.toJSONString();
        final byte[] contentBytes = r.getBytes(Charset.forName("utf-8"));

        String uri = "/green/image/scan";

       String stringMono =  client.post().uri(uri).headers(builder -> {
           builder.set("Accept", "application/json");
           builder.set("Content-Type", "application/json;charset=utf-8");
           builder.set("x-acs-version", "2018-05-09");
           builder.set("x-acs-signature-method", signer.getSignerName());
           builder.set("x-acs-signature-version", signer.getSignerVersion());
           builder.set("bizType", "odin_im");

           builder.set("Date", ParameterHelper.getRFC2616Date(null));
           builder.set("x-acs-signature-nonce", signer.getSignerNonce());

           // 签名
           String strMd5 = ParameterHelper.md5Sum(contentBytes);
           builder.set("Content-MD5", strMd5);
           builder.set("Content-Length", String.valueOf(contentBytes.length));

           // 加密
           String strToSign = composer.composeStringToSign("POST", uri, builder, signer,
                   Collections.emptyMap());

           logger.debug("ali check picture sign str:{}", strToSign);

           String signature = signer.signString(strToSign, acsCredentials);
           builder.set("Authorization",
                   "acs " + acsCredentials.getAccessKeyId() + ":" + signature);

        }).body(Mono.just(r), String.class).retrieve().bodyToMono(String.class).toFuture().get();

       logger.debug("get string: {}", stringMono);
    }

    private static final byte[] EMPTY_DATA = new byte[0];


    private Mono<byte[]> getImage(String imageName) {
        return Mono.fromSupplier(() -> {
            logger.debug("load resize image:{} start...", imageName);
            return EMPTY_DATA;
        }).onErrorReturn(EMPTY_DATA);
    }
}

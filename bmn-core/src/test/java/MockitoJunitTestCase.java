import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: zyq
 * @date: 2019/1/14
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoJunitTestCase {

    private static final String REGEX = ".*[0-9一二三四五六七八九零幺星井]{5,}.*";

    private static final String D_ID = "localhost";
    private static final String CHANNEL_CONTEXT = "";


    @Mock
    private PropertiesHolder propertiesHolder;

    @Spy
    TimeoutManager timeoutManager;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private RedisClient redis;

    private ResultSelectHandler handler;

    private Channel channel;

    @Before
    public void init() {
        // 需要测试的类，一般设置为真实对象
        handler = spy(new ResultSelectHandler(propertiesHolder, REGEX, timeoutManager));

        ChannelContext channelContext = spy(ChannelContext.class);
        DContext dContext = spy(new ADContext(0, 10, 100));

        // channel 内部的对象也自动设置成mock对象
        channel = mock(Channel.class, RETURNS_DEEP_STUBS);

        // 创建stub
        when(ctx.channel()).thenReturn(channel);
        when(ctx.channel().attr(CHANNEL_CONTEXT).get()).thenReturn(channelContext);

        Set<String> asrs = new HashSet<>(2);
        asrs.add("ni");
        asrs.add("nike");
        when(propertiesHolder.getPriority()).thenReturn(asrs);

        List<String> orderAsrs = new ArrayList<>(2);
        orderAsrs.add("ni");
        when(propertiesHolder.getPriorityOrder()).thenReturn(orderAsrs);

        channelContext.setDId(D_ID);
    }

    @Test
    public void write() {
        ImmutableList<Object> msgs = ImmutableList.builder()
            .add(buildResult("ni", 0, "[ni_未准备好]"))
            .add(buildResult("nike", 0, "[nike-未准备好]"))
            .add(buildResult("ni", 1, "[ni-准备好]"))
            .add(buildResult("nike", 1, "[nike-准备好]"))
            .build();

        msgs.forEach((msg) -> {
            try {
                handler.write(ctx, msg, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void write2() throws Exception {
        this.write();

        ChannelContext channelContext = (ChannelContext) ctx.channel().attr(CHANNEL_CONTEXT).get();
        // 通过spring返回类获取属性值
        String redisKey = ReflectionTestUtils
            .getField(channelContext, ChannelContext.class, "redisKey").toString();

        // 验证的同时捕获参数值
        ArgumentCaptor<Object> argument = ArgumentCaptor.forClass(Object.class);
        verify(redis).set(eq(redisKey), argument.capture());
        // 验证timeout方法是否被调用
        verify(channelContext).timeout();
        verify(timeoutManager).check(eq(ctx), any());

        // 拿到捕获的参数值做为后续返回
        when(redis.get(redisKey)).thenReturn(argument.getValue());
        // 验证参数值
        assertEquals("0", argument.getValue());

        // 验证方法调用结果（验证带状态的结果。要求对象是真实对象）
        assertEquals(channelContext.calling(), false);

        // 通过配置前置回答，来为spy定义的真实对象进行mock。而不会调用真实方法。如果是后置回答则会直接真实方法
        doAnswer(invocation -> {
            String text = invocation.getArgument(0);
            if ("下一个".equals(text)) {
                return true;
            }

            if ("退出".equals(text)) {
                return true;
            }

            return false;
        }).when(timeoutManager).contains(anyString());
    }


    private static Object buildResult(String p, int e, String t) {
        return MsgResult.builder().e(e);
    }


    private static class ResultSelectHandler {

        public ResultSelectHandler(PropertiesHolder propertiesHolder, String regex,
            TimeoutManager timeoutManager) {
        }

        public void write(ChannelHandlerContext ctx, Object msg, Object o) {

        }
    }

    private static class PropertiesHolder {

        public Set<String> getPriority() {
            return null;
        }

        public List<String> getPriorityOrder() {
            return null;
        }
    }

    private static class TimeoutManager {

        public void check(ChannelHandlerContext eq, Object any) {

        }

        public void contains(String did) {

        }
    }


    private static class Channel {

        public Attribute attr(String asrContext) {
            return null;
        }
    }

    private static class Attribute {

        public Object get() {
            return null;
        }
    }


    private static class RedisClient {

        public void set(String eq, Object capture) {

        }

        public Object get(String k) {
            return null;
        }
    }

    private static class ChannelHandlerContext {

        public Channel channel() {
            return null;
        }
    }

    private static class ChannelContext {

        public void setDId(String dId) {
        }

        public void timeout() {

        }

        public boolean calling() {
            return false;
        }
    }

    private static class ADContext extends DContext {

        public ADContext(int s, long t1,
            long t2) {
        }
    }

    private static class DContext {

    }

    private static class MsgResult {

        public static MsgResult builder() {
            return null;
        }


        public MsgResult e(int e) {
            return null;
        }
    }

    private static class ReflectionTestUtils {

        public static Object getField(Object target,
            Class<?> clazz, String name) {
            return null;
        }
    }
}

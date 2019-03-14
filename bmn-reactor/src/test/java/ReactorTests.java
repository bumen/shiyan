import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * @author: zyq
 * @date: 2019/3/6
 */
public class ReactorTests {

    private Map<String, UserInfo> cache = new HashMap<>();

    @Test
    public void testNow() throws InterruptedException {
        cache.put("123", new UserInfo("123", 1));
        cache.put("456", new UserInfo("456", 2));

        String key = "123";
        Mono.just(key)
            .timeout(Duration.ofMillis(500))
            .flatMap(s -> {
            return Mono.just(Tuples.of(s, "123"));
        }).subscribe(item->{
            String v = item.getT1();
            String vv = item.getT2();

            System.out.println("v :" + v + "--- " + vv);
        });

        Thread.sleep(3000);


    }

    @Test
    public void testFlux() throws InterruptedException {
        Flux.range(1,10).publishOn(Schedulers.parallel()).log().subscribe();
        TimeUnit.MICROSECONDS.sleep(1000);
    }

    @Test
    public void testParallelFlux() throws InterruptedException {
        Flux.range(1, 10).parallel(5).runOn(Schedulers.parallel()).log().subscribe();

        TimeUnit.MICROSECONDS.sleep(1000);
    }



    private static class UserInfo {
        public long time;
        public String name;

        public UserInfo(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }
}

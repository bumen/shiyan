import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

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
        }).repeat().subscribe(item->{
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

    @Test
    public void testMono() {
        Mono<String> mono = Mono
            .fromSupplier(() -> 3)
            .switchIfEmpty(Mono.just(10))
            .flatMapMany(token -> Flux.fromArray(new Object[]{1,2,3}))
            .take(10)
            .publishOn(Schedulers.parallel())
            .flatMap(item -> Flux.just(111))
            .map(item -> "ok")
            .collectList()
//            .timeout(Duration.ofMillis(900)), 不进行超时判断，只要有结果返回就放在cache中缓存用于下次查询
            .map(list -> "success")
            .onErrorReturn("error");
        mono.subscribe();
    }

    @Test
    public void testMonoSupplier() {
        Mono<Integer> mono = Mono
            .fromSupplier(() -> 3)
            .switchIfEmpty(Mono.just(10));
        mono.subscribe();
    }

    @Test
    public void testMinute() {
        long d = 1561018958000l;

        System.out.println(getTimeAccuratetoMinute(d));
    }

    /** 获取一个时间精确到分钟的数值12:12:12 ->12:12:00 */
    public static long getTimeAccuratetoMinute(long oldTimeMS) {
        return oldTimeMS - oldTimeMS % 60000;
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

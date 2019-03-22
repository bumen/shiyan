package com.bmn.springboot.client.controller;

import com.bmn.springboot.client.pojo.WorldDTO;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() throws ExecutionException, InterruptedException {
        log.debug("success get hello");

        WorldDTO world = new WorldDTO();
        world.setCode("hello");
        CompletableFuture<String> future = this.recognizeCore(world, String.class);
        log.debug("success get world: {}", future.get());
        return "hello boot";
    }


    private WebClient client = WebClient.create("http://127.0.0.1:11011");

    private static final String PATH ="/world";

    private <T> CompletableFuture<T> recognizeCore(WorldDTO world, Class<T> clazz) {
        return client.post().uri(PATH)
            .contentType(MediaType.APPLICATION_JSON).body(Mono.just(world), WorldDTO.class)
            .retrieve()
            .bodyToMono(clazz).toFuture();
    }
}

package com.agripredictor.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class GatewayApplication implements CommandLineRunner {
    @Resource
    private ConfigurableEnvironment springEnv;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }



    @Override
    public void run(String... args) throws Exception {
        MutablePropertySources propSrcs = springEnv.getPropertySources();
        // 获取所有配置
        Map<String, String> props = StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), springEnv::getProperty));

        // key 和 value 之间的最小间隙
        int interval = 20;
        int max = props.keySet().stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("")
                .length();

        // 打印
        props.keySet().stream()
                .sorted()
                .forEach(k -> {
                    int i = max - k.length() + interval;
                    String join = String.join("", Collections.nCopies(i, " "));
                    System.out.println(String.format("%s%s%s", k, join, props.get(k)));
                });
    }


}

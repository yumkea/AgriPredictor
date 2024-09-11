package com.agripredictor.gateway.fiter;

import com.agripredictor.gateway.utlis.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("拦截器开始执行...");
        ServerHttpRequest request = exchange.getRequest();
        if (checkURL(request.getPath().toString())) {
            log.info("放行 {}", request.getPath());
            return chain.filter(exchange); //
        }
        String token = request.getHeaders().getFirst("token");
        if(request.getPath().toString().contains("/forum/pageForum") && token == null){
            log.info("未登录查询论坛帖子，放行 {}", request.getPath());
            return chain.filter(exchange);
        }


        if(request.getPath().toString().contains("/forum/pageReplyForum") && token == null){
            log.info("未登录查询论坛评论，放行 {}", request.getPath());
            return chain.filter(exchange);
        }

        if(request.getPath().toString().contains("/oss//download") && token == null){
            log.info("未登录获取文件，放行 {}", request.getPath());
            return chain.filter(exchange);
        }

        String id = null;
        if (token != null) {

            try {
                id = JWTUtils.getId(token).toString();
                log.info("拦截器解析到id 为 {}", id);
            } catch (Exception e) {
                log.info("解析失败");
                ServerHttpResponse response = exchange.getResponse();
                response.setRawStatusCode(401);
                return response.setComplete();
            }
        } else {
            log.info("token为空");
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }

        //修改请求

        String finalId = id;
        ServerWebExchange swe = exchange.mutate()
                .request(builder ->
                        builder.header("userId", finalId)).build();

        return chain.filter(swe);
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private boolean checkURL(String url) {
        return url.contains("/user/login") || url.contains("/user/register");
    }


}
package com.agripredictor.gateway.utlis;


import com.agripredictor.common.constant.UserConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;


@RequiredArgsConstructor
public class JWTUtils {

    private static final String key = "agri_predictor";
    private static final long expire = 12 * 60 * 60 * 1000L;

    public static String generateJwt(Map<String, Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }


    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }


    /**
     * 获取id
     * @param jwt
     * @return
     */
    public static Long getId(String jwt){
        Claims claims =parseJWT(jwt);
        long userId = Long.parseLong(claims.get(UserConstant.USER_ID).toString());
        return userId;
    }

}

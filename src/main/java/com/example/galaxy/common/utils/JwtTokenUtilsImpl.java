//jwt工具类
package com.example.galaxy.common.utils;

import com.example.galaxy.security.service.impl.AuthUserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.galaxy.common.Constants.TOKEN_KEY;

/**
 * JWT生成令牌、验证令牌、获取令牌
 */
@Component
public class JwtTokenUtilsImpl implements JwtTokenUtils {

    //私钥
    private static final String SECRET_KEY = "coding";

    // 过期时间 毫秒,设置默认12个小时过期
    private static final long EXPIRATION_TIME = 3600000L * 12;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    @Override
    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    @Override
    public Boolean isTokenExpired(String token) throws Exception {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 刷新令牌(逻辑错误，停用）
     *
     * @param token 原令牌
     * @return 新令牌
     */
    @Override
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        AuthUserDetails user = (AuthUserDetails) userDetails;
        String username = getUsernameFromToken(token);

        // 检查 Redis 中是否存在该令牌
        String redisKey = TOKEN_KEY + username;
        String redisToken = redisUtils.get(redisKey).toString();
        if (redisToken == null || !redisToken.equals(token)) {
            return false; // Redis 中不存在或令牌不匹配
        }

        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) throws Exception {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            new Throwable(e);
        }
        return claims;
    }
}

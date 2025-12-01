package vn.web.Services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.security.auth.message.ClientAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.web.Common.TokenType;
import vn.web.Services.JwtService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryMinute}")
    private long expiryMinute;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey ;

    @Override
    public String generateAccessToken(long userid, String username, List<String> authorities) {
        Map<String , Object> claims = new HashMap<>();
        claims.put("userid" , userid);
        claims.put("role" , authorities);
        return generateToken(claims , username);
    }

    @Override
    public String generateRefreshToken(long userid, String username, List<String> authorities) {
        Map<String , Object> claims = new HashMap<>();
        claims.put("userid" , userid);
        claims.put("role" , authorities);
        return generateRefreshToken(claims , username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaims(TokenType.ACCESS_TOKEN , token , Claims::getSubject);
    }



    private <T> T extractClaims(TokenType type , String token , Function<Claims , T> claimsTExtractor){
        final Claims claims  = extractAllClaims(token , type);
        return claimsTExtractor.apply(claims);
    }

    private Claims extractAllClaims(String token , TokenType type){
        try{
            return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody();
        }
        catch (SignatureException | ExpiredJwtException e){
            throw  new RuntimeException("error");
        }
    }


    private String generateToken(Map<String , Object> claims , String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryMinute))
                .signWith( getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String , Object> claims , String username){
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith( getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type){
        switch (type){
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new RuntimeException("error");
        }
    }

}

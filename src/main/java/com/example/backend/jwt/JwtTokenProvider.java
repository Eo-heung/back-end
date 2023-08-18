package com.example.backend.jwt;

import com.example.backend.entity.TokenBlacklist;
import com.example.backend.entity.User;
import com.example.backend.exception.InvalidTokenException;
import com.example.backend.repository.TokenBlacklistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.backend.exception.InvalidTokenException;

@Component
public class JwtTokenProvider {
    //JWT Token의 signature의 유효성 검사에 사용될 키 값
    //BASE64 인코딩된 값
    //navercloud5todobootappgogibitcamp702
    private static final String SECRET_KEY = "bmF2ZXJjbG91ZDV0b2RvYm9vdGFwcGdvZ2liaXRjYW1wNzAy";
    //SECRET_KEY를 Key 객체로 변환
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /*
    * 사용자 정보를 받아서 JWT Token을 생성해주는 메소드
    * JSON 문자열을 Base64 인코딩하고 뒷부분에 서버에 있는 시그니쳐(SECRET_KEY)를 Hashing해서 추가
    * */
    public String create(User user) {
        //토큰 만료일 설정. 현재로부터 1일뒤로 설정
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        
        //JWT Token 생성하여 반환
        return Jwts.builder()
                    //시그니쳐(서명)부분에 들어갈 key값 지정
                   .signWith(key, SignatureAlgorithm.HS256)
                   //페이로드에 들어갈 내용
                   //토큰의 주인(sub)
                   .setSubject(user.getUserId())
                   //토큰 발행주체(iss) 임의로 지정
                   .setIssuer("todo boot app")
                   //토큰 발행일자(isa)
                   .setIssuedAt(new Date())
                   //토큰 만료일자
                   .setExpiration(expireDate)
                   //토큰 발행
                   .compact();
    }

    //JWT Token의 유효성 확인하는 메소드
    //subject에 담겨있는 username을 리턴한다.
    public String validateAndGetUsername(String token) {
        //받아온 토큰 값을 파싱해서 유효성 검사
        //토큰에 있는 시그니쳐와 서버에서 가지고있는 시그니쳐값 비교
        //시그니쳐 비교해서 일치하면 Claims 객체를 리턴
        //userId 반환
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(SECRET_KEY.getBytes())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        //subject에 담겨있는 userId를 리턴
        return claims.getSubject();
    }

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository; // 인스턴스 주입

    public String validateAndGetUsername(String token, HttpServletRequest request) { // HttpServletRequest 추가
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 여기에 위에서 쓰였던 로직 추가
        String headerToken = request.getHeader("Authorization").split(" ")[1];
        Optional<TokenBlacklist> blacklistedToken = tokenBlacklistRepository.findByToken(headerToken); // 수정된 부분
        if (blacklistedToken.isPresent()) {
            throw new InvalidTokenException("Token has been invalidated");
        }

        // subject에 담겨있는 userId를 리턴
        return claims.getSubject();
    }

}

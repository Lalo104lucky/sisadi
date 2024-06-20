package issste.gob.mx.SISADI.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import issste.gob.mx.SISADI.security.entity.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtProvider {
    @Value("IILZuJpbMlDgbWTwkn5RZq6jIX3HwXEINt0jQ9BFzBs")
    private String secret;
    @Value("604800")
    private long expiration;
    private final String TOKEN_HEADER = "Authentication";
    private final String TOKEN_TYPE ="Bearer ";

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication auth){
        UserDetailsImpl usuario = (UserDetailsImpl) auth.getPrincipal();
        Claims claims = Jwts.claims().setSubject(usuario.getUsername());
        claims.put("id_usuario", usuario.getId_usuario());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(
                tokenCreateTime.getTime() + expiration * 1000
        );
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(tokenValidity)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_TYPE)){
            return bearerToken.replace(TOKEN_TYPE, "");
        }
        //bearerToken.substring(TOKEN_TYPE.length());
        return null;
    }

    public Claims resolveClaims(HttpServletRequest req){
        try{
            String token = resolveToken(req);
            if(token != null){
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException e){
            throw e;
        } catch (Exception e){
            throw e;
        }
    }

    public boolean validateClaims(Claims claims, String token){
        try {
            parseJwtClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

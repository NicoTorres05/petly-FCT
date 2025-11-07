package petly.seguridad.jwt;

import petly.token.TokenBlackListService;

import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
@Primary
public class JwtDecoderC implements JwtDecoder {
    private final JwtDecoder delegate;
    private final TokenBlackListService blackListService;

    public JwtDecoderC(JwtDecoder delegate, TokenBlackListService blackListService) {
        this.delegate = delegate;
        this.blackListService = blackListService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        if (blackListService.isRevoked(token)) {

            throw new JwtException("Token en lista negra");
        }
        return delegate.decode(token);
    }
}
package petly.logout;

import petly.token.TokenBlackListService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public class JwtBlacklistLogoutHandler implements LogoutHandler {

    private final TokenBlackListService tokenBlackListService;

    public JwtBlacklistLogoutHandler(TokenBlackListService tokenBlackListService) {
        this.tokenBlackListService = tokenBlackListService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = request.getHeader("Authorization");
        System.out.println("Token agregado a la lista negra: " + token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenBlackListService.blacklistToken(token);
        }

        System.out.println("Sesión cerrada correctamente");
    }
}
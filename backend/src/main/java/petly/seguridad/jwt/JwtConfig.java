package petly.seguridad.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
@Setter
public class JwtConfig {

    @Value("${app.jwt-secret}")
    private String secretKey;

    @Getter
    @Value("${app.jwt-expiration-ms}")
    private long jwtExpiration;

    @Value("${security.jwt.algorithm}")
    private String algorithm;

    public SecretKey getSecretKey() {
        var key =new OctetSequenceKey.Builder(secretKey.getBytes())
                .algorithm(new JWSAlgorithm(algorithm))
                .build();
        return key.toSecretKey();
    }

    public JWSAlgorithm getAlgorithm() {
        return new JWSAlgorithm(algorithm);
    }

}
package br.com.rapps.seguranca;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/***
 * Obter dados do cliente. Estes dados podem vir de um banco de dados.
 * Não é de responsabilidade do oauth gerenciar usuarios
 */
@Service
public class RASClientDetailsService implements ClientDetailsService {
    private static final String REDIRECT_URI = "http://localhost:8080/remote-application-services/index.html";
    private static final List<String> GRANT_TYPES = Arrays.asList("password","authorization_code","refresh_token","implicit","redirect");
    private static final List<String> SCOPES = Arrays.asList("read", "read-write");
    private static final String USUARIO = "erico";
    private static final String SENHA = "erico1234";
 
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws OAuth2Exception {
        if (USUARIO.equals(clientId)) {
            Set<String> uri = new HashSet<>();
            uri.add(REDIRECT_URI);
            
            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(USUARIO);
            clientDetails.setClientSecret(SENHA);
            clientDetails.setAuthorizedGrantTypes(GRANT_TYPES);
            clientDetails.setScope(SCOPES);
            clientDetails.setRegisteredRedirectUri(uri);
 
            return clientDetails;
        }
        else {
            throw new NoSuchClientException("Cliente com id " + clientId + " não reconhecido");
        }
    }
}

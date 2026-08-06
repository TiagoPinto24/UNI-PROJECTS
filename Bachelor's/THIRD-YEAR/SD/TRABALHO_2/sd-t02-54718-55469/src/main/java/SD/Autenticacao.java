package SD;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import java.io.IOException;

@Provider
public class Autenticacao implements ContainerRequestFilter {

    //chamado a cada pedido
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");
        String encodedCredentials = authHeader.substring("Basic ".length());
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] credenciais = decodedCredentials.split(":"); //1 posição: username, 2 posição: password
        
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            Unauthorized(requestContext);
            return;
        }

        if (credenciais.length != 2 || !validUser(credenciais[0], credenciais[1])) {
            Unauthorized(requestContext);
        }
    }

    private boolean validUser(String username, String password) {
        return ("Joao".equals(username) && "Joao23".equals(password))
            || ("Tiago".equals(username) && "2402".equals(password))
            || ("Administrador".equals(username) && "trabalho2SD".equals(password));
    }

    private void Unauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .entity("Unauthorized: Invalid credentials").build());
    }
}
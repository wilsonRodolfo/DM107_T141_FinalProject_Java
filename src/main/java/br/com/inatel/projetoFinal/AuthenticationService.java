package br.com.inatel.projetoFinal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.StringTokenizer;

public class AuthenticationService {
    public boolean authenticate(String credentials) throws SQLException {
        
        System.out.println(credentials);

        if (null == credentials)
            return false;
        // extraindo o valor vindo do header "Basic encodedstring" for Basic
        //Exemplo: "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = credentials.replaceFirst("Basic"+ " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String usernameAndPassSplit[] = usernameAndPassword.split(":");
        final String username = usernameAndPassSplit[0];
        final String password = usernameAndPassSplit[1];

        // Estamos usando um unico usuario e senha, aqui poderia ser feito via banco de dados
        boolean authenticationStatus = true;
        
        if(!username.equals("admin") || !password.equals("admin")) {
        	EntregaDAO bd = new EntregaDAO();
        	authenticationStatus = bd.validaCliente(username, password);
        }
        	
        return authenticationStatus;
    }

}
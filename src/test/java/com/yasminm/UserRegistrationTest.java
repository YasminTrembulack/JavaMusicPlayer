package com.yasminm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import com.yasminm.model.UserData;
import com.yasminm.util.Authentication;
import com.yasminm.util.HibernateUtil;

// mvn clean test

public class UserRegistrationTest {
 @Test
    public void testAuthenticateUser() {

        // ---- USER REGISTRATION ---- 
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();

        Transaction transaction = session.beginTransaction();

        UserData user = new UserData();
        user.setUsername("usuario_existente");
        user.setPassword("senha_correta");
        session.save(user);
        transaction.commit();
        // ------------------------


        
        // Teste quando o usuário não existe
        Authentication auth = Authentication.authenticateUser("usuario_inexistente", "senha_incorreta");
        assertNull(auth.getUser());
        assertFalse(auth.getUserExists());
        
        // Teste quando o usuário existe, mas a senha está incorreta
        auth = Authentication.authenticateUser("usuario_existente", "senha_incorreta");
        assertNull(auth.getUser());
        assertTrue(auth.getUserExists());
        

        // Teste quando o usuário existe e a senha está correta
        auth = Authentication.authenticateUser("usuario_existente", "senha_correta");
        assertNotNull(auth.getUser());
        assertTrue(auth.getUserExists());



        
        // // Teste quando o usuário existe e a senha está correta
        // user = new UserData();
        // user.setUsername("usuario_existente");
        // user.setPassword("senha_correta");
        
        // // Simulando a existência do usuário no banco de dados
        // // Você pode substituir isso com mocks ou um banco de dados de teste
        // // para testes de integração mais realistas
        // auth.setUser(user);

        // auth = Authentication.authenticateUser("usuario_existente", "senha_correta");
        // assertNotNull(auth.getUser());
        // assertTrue(auth.getUserExists());
    }
}

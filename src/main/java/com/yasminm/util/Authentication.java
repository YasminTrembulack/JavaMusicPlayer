package com.yasminm.util;

import com.yasminm.model.UserData;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class Authentication {
    private UserData user = null;
    private Boolean userExists = false;

    public static Authentification authenticateUser(String usernameInput, String passwordInput) {
        Authentification auth = new Authentification();

        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from UserData u where u.username = :user");
        query.setParameter("user", usernameInput);

        List<UserData> users = query.list();
        transaction.commit();

        if(users.size() == 0) return auth;

        auth.userExists = true;
        UserData loggedUser = users.get(0);

        if (!loggedUser.getPassword().equals(passwordInput)) return auth;

        auth.user = loggedUser;

        return auth;
    }
}

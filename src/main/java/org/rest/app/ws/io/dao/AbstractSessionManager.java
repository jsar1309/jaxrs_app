package org.rest.app.ws.io.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractSessionManager {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public void persist(Object o){
        getSession().persist(o);
    }

    public void update(Object o){
        getSession().update(o);
    }

}

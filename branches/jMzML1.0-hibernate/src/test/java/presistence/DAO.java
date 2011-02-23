package presistence;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 08/02/11
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class DAO {
    private static final Logger logger = Logger.getLogger(DAO.class);
    private static final SessionFactory sessionFactory = new Configuration().configure("META-INF/hibernate.cfg.xml").buildSessionFactory();

    private static final ThreadLocal<Session> session = new ThreadLocal<Session>();

    protected DAO() {
    }

    public static Session getSession() {
        Session session = DAO.session.get();

        if (session == null) {
            session = DAO.sessionFactory.openSession();
            DAO.session.set(session);
        }
        return session;
    }

    protected static void begin() {
        getSession().beginTransaction();
    }

    protected static void commit() {
        getSession().getTransaction().commit();
    }

    protected static void rollBack() {
        try {
            getSession().getTransaction().rollback();
        } catch(HibernateException e) {
            logger.log(Level.WARN, "Cannot rollback", e);
        }

        try {
            getSession().close();
        } catch(HibernateException e) {
            logger.log(Level.WARN, "Cannot close", e);
        }

        DAO.session.set(null);
    }

    public static void close() {
        getSession().close();
        DAO.session.set(null);
    }
}

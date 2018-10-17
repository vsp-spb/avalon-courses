package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

@Singleton
public class ConnectionBean implements ConnectionBeanLocal {

    private Connection con;

    private final static String URL = "jdbc:derby://localhost:1527/jhelp";
    private final static String USER = "demo";
    private final static String PASSWORD = "demo";

    @PostConstruct
    public void init() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("ConnectionBean. Error creating connection. " + ex.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        return con;
    }
    
    @PreDestroy
    public void destroy(){
        if(con != null){
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("ConnectionBean. Error closing connection. " + ex.getMessage());
            }
        }
    }
}

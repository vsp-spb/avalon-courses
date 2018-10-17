package data;

import java.sql.Connection;
import javax.ejb.Local;

@Local
public interface ConnectionBeanLocal {
    Connection getConnection();
}

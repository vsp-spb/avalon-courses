package data;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Local;

@Local
public interface DataBeanLocal {
    List<Definition> getTermDefinitions(String term) throws SQLException;
    
    boolean add(String term, String definition)  throws SQLException;
    
    boolean updateDefinition(Definition definition)  throws SQLException;
    
    boolean delete(String term)  throws SQLException;
}

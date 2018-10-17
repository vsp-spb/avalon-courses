package msg;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean
public class CollectionBean {
    private Set<String> logins;
    
    @PostConstruct
    public void init(){
        logins = new HashSet<>();
    }
    
    public void  add(String login){
        logins.add(login);
    }
    
    public void remove(String login){
        logins.remove(login);
    }
    
    public Set<String> getList(){
        return logins;
    } 
}

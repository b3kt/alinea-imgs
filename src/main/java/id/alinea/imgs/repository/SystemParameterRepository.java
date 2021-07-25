package id.alinea.imgs.repository;

import id.alinea.imgs.entity.SystemParameter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemParameterRepository implements PanacheRepository<SystemParameter> {
    
    public String getValueByCode(String code) {
        SystemParameter sp =  find("code", code).firstResult();
        return sp != null ? sp.value : null;
    }

}

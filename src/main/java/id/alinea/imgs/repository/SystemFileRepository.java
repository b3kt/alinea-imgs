package id.alinea.imgs.repository;

import id.alinea.imgs.entity.SystemFile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemFileRepository implements PanacheRepository<SystemFile> {
    
    public SystemFile findByFileUid(String fileId){
        return find("file_uuid", fileId).firstResult();
    }
}

package id.alinea.imgs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="system_parameters")
public class SystemParameter extends BaseEntity{
    
    @Column
    public String code;

    @Column
    public String value;

    @Column
    public String type;

}

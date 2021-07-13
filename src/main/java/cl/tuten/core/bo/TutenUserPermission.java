
package cl.tuten.core.bo;

import cl.tuten.commons.constants.DatabaseSchema;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Permiso de un usuario admin (backoffice). Se hace uso de esto en el ACL.
 * 
 * @author Gus
 */
@Entity
@Table(name = "tuten_user_permission", schema = DatabaseSchema.SCHEMA_LOGIN)
@XmlRootElement
public class TutenUserPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String PERMISSION_TYPE_VIEW = "VIEW";
    public static final String PERMISSION_TYPE_EXECUTE = "EXECUTE";
    
    @Id
    @Basic(optional = false)
    @Column(name = "permission_id")
    private String permissionId;
    
    @NotNull
    @Column(name = "description")
    private String description;
    
    @NotNull
    @Column(name = "section")
    private String section;
    
    @NotNull
    @Column(name = "permission_type")
    private String permissionType;
    
    @Transient
    private Boolean selected = false;
    
    public TutenUserPermission() {
    }

    public TutenUserPermission(String permissionId, String description, String section, String permissionType) {
        this.permissionId = permissionId;
        this.description = description;
        this.section = section;
        this.permissionType = permissionType;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionId != null ? permissionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutenUserPermission)) {
            return false;
        }
        TutenUserPermission other = (TutenUserPermission) object;
        if ((this.permissionId == null && other.permissionId != null) || (this.permissionId != null && !this.permissionId.equals(other.permissionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.tuten.core.bo.TutenUserPermission[ permissionId=" + permissionId + " ]";
    }
    
}

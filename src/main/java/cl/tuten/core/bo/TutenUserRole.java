
package cl.tuten.core.bo;

import cl.tuten.commons.constants.DatabaseSchema;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Rol de un usuario admin (backoffice). Se hace uso de esto en el ACL.
 * 
 * @author Gus
 */
@Entity
@Table(name = "tuten_user_role", schema = DatabaseSchema.SCHEMA_LOGIN)
@XmlRootElement
public class TutenUserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "role_id")
    private String roleId;
    
    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "timeout")
    private Integer timeout;
    
    @NotNull
    @Column(name = "multiple_sessions_allowed")
    private Boolean multipleSessions;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;
    
    @ManyToMany
    @JoinTable(
      name="tuten_role_permission",
      joinColumns=@JoinColumn(name="role_id", referencedColumnName="role_id"),
      inverseJoinColumns=@JoinColumn(name="permission_id", referencedColumnName="permission_id"),
            schema = DatabaseSchema.SCHEMA_LOGIN)
    private List<TutenUserPermission> permissions;
    
    public TutenUserRole() {
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getMultipleSessions() {
        return multipleSessions;
    }

    public void setMultipleSessions(Boolean multipleSessions) {
        this.multipleSessions = multipleSessions;
    }

    public List<TutenUserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<TutenUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutenUserRole)) {
            return false;
        }
        TutenUserRole other = (TutenUserRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.tuten.core.bo.TutenUserPermission[ roleId=" + roleId + " ]";
    }
    
}


package cl.tuten.core.bo;

import cl.tuten.commons.constants.DatabaseSchema;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Entity
@Table(name = "tuten_administrator", schema = DatabaseSchema.SCHEMA_LOGIN)
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TutenAdministrator.findAll", query = "SELECT t FROM TutenAdministrator t")
        , @NamedQuery(name = "TutenAdministrator.findByUsername", query = "SELECT t FROM TutenAdministrator t WHERE t.username = :username")
        , @NamedQuery(name = "TutenAdministrator.findByUserId", query = "SELECT t FROM TutenAdministrator t WHERE t.userId = :userId")
        , @NamedQuery(name = "TutenAdministrator.findByEmail", query = "SELECT t FROM TutenAdministrator t WHERE t.email = :email")
        , @NamedQuery(name = "TutenAdministrator.findByFirstName", query = "SELECT t FROM TutenAdministrator t WHERE t.firstName = :firstName")
        , @NamedQuery(name = "TutenAdministrator.findByLastName", query = "SELECT t FROM TutenAdministrator t WHERE t.lastName = :lastName")})
public class TutenAdministrator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "username")
    private String username;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "password_hash")
//TODO: mientras se usará password normal, hasta que se implemente la creación de usuarios
    @JsonIgnore
    private String passwordHash;
    @Size(max = 2147483647)
    @Column(name = "session_token")
    private String sessionToken;

    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private String sessionStatus;

    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private TutenUserRole role;

    @Column(name = "lastest_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastestActivityDate;

    public TutenAdministrator() {
    }

    public TutenAdministrator(Long userId) {
        this.userId = userId;
    }

    public TutenAdministrator(Long userId, String username, String firstName, String lastName, String passwordHash, boolean passwordNeedsReset) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonIgnore
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }


    public TutenUserRole getRole() {
        return role;
    }

    public void setRole(TutenUserRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Date getLastestActivityDate() {
        return lastestActivityDate;
    }

    public void setLastestActivityDate(Date lastestActivityDate) {
        this.lastestActivityDate = lastestActivityDate;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutenAdministrator)) {
            return false;
        }
        TutenAdministrator other = (TutenAdministrator) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.tuten.core.bo.TutenAdministrator[ userId=" + userId + " ]";
    }

   }

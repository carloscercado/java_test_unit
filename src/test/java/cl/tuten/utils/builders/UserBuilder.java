package cl.tuten.utils.builders;

import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.bo.TutenUserPermission;
import cl.tuten.core.bo.TutenUserRole;
import cl.tuten.core.utils.Token;
import cl.tuten.core.utils.TutenUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Cercado
 */
public class UserBuilder extends Builder<TutenAdministrator>{
    private List<String> permissions = new ArrayList<>();

    public static UserBuilder getBuilder() {
        return new UserBuilder();
    }

    private UserBuilder(){
        instance = new TutenAdministrator();
        this.create();
    }

    private UserBuilder create(){
        this.instance.setEmail("benito@perez.com");
        this.instance.setFirstName("benito");
        this.instance.setLastName("perez");
        this.instance.setUsername("bperez");
        this.instance.setActive(Boolean.TRUE);
        this.instance.setSessionToken("#");
        this.instance.setUserId(1L);
        this.instance.setPasswordHash("#");
        this.instance.setSessionStatus("ACTIVE");

        return this;
    }

    public UserBuilder withPassword(String password){
        final String hashPassword = Token.getStringHash(password);
        this.instance.setPasswordHash(hashPassword);
        return this;
    }

    public UserBuilder disabled(){
        this.instance.setActive(Boolean.FALSE);
        return this;
    }

    public UserBuilder logout(){
        this.instance.setSessionStatus("INACTIVA");
        return this;
    }

    public UserBuilder withPermission(String permission){
        this.permissions.add(permission);
        return this;
    }

    public UserBuilder withUsername(String username){
        this.instance.setUsername(username);
        return this;
    }

    public UserBuilder withId(Long id){
        this.instance.setUserId(id);
        return this;
    }

    public UserBuilder withRole(TutenUserRole role){
        this.instance.setRole(role);
        return this;
    }

    @Override
    public TutenAdministrator build(EntityManager entityManager){

        if(this.instance.getRole() == null){
            this.instance.setRole(RoleBuilder.getBuilder().build(entityManager));
        }

        this.instance.setSessionToken(TutenUtils.createJWT(this.instance, 15L));

        entityManager.getTransaction().begin();

        entityManager.persist(instance);

        entityManager.flush();

        if(!permissions.isEmpty()){
            for(String perm: permissions){
                final TutenUserPermission permission = new TutenUserPermission();
                permission.setPermissionId(perm);
                permission.setDescription("#####");
                permission.setPermissionType("#####");
                permission.setSection("#####");
                entityManager.persist(permission);

                entityManager.createNativeQuery("INSERT INTO mss_login.tuten_role_permission (role_id, permission_id) values(?1, ?2);")
                             .setParameter(1, this.instance.getRole().getRoleId())
                             .setParameter(2, permission.getPermissionId())
                             .executeUpdate();

                entityManager.flush();

            }

        }

        entityManager.getTransaction().commit();


        return instance;
    }

}

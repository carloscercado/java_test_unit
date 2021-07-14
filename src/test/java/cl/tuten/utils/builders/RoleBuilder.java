package cl.tuten.utils.builders;

import cl.tuten.core.bo.TutenUserRole;

/**
 * @author Carlos Cercado
 */
public class RoleBuilder extends Builder<TutenUserRole>{

    public static RoleBuilder getBuilder() {
        return new RoleBuilder();
    }

    public RoleBuilder(){
        instance = new TutenUserRole();
        this.create();
    }

    public RoleBuilder disabled(){
        this.instance.setActive(Boolean.FALSE);
        return this;
    }

    public RoleBuilder withName(String name){
        this.instance.setName(name);
        return this;
    }

    public RoleBuilder withRoleID(String id){
        this.instance.setRoleId(id);
        return this;
    }

    public RoleBuilder multipleSession(Boolean op){
        this.instance.setMultipleSessions(op);
        return this;
    }

   private RoleBuilder create(){
        this.instance.setActive(Boolean.TRUE);
        this.instance.setRoleId("1");
        this.instance.setName("SUPERADMIN");
        this.instance.setTimeout(10000);
        this.instance.setMultipleSessions(Boolean.FALSE);
        return this;
    }
}

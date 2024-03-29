/*
 * Brugge Framework.
 */
package io.codeffeine.brugge.domain.migration.data;

import io.codeffeine.brugge.domain.migration.InitData;
import io.codeffeine.brugge.domain.security.entity.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mirko Gueregat @mgueregath <mgueregath@codeffeine.io>
 */
public class Roles extends InitData {

    public static final int DEVELOPMENT = 9990;
    public static final int PUBLIC = 7999;
    public static final int PRIVATE = 8999;
    private static final Map<Integer, Role> roles = new HashMap<>();

    public Roles() {
        super();
        roles.put(PUBLIC, new Role(PUBLIC, "Público", false, false));
        roles.put(PRIVATE, new Role(PRIVATE, "Privado", false, false));
        roles.put(DEVELOPMENT, new Role(DEVELOPMENT, "Desarrollo", false, false));
    }

    public static List<Role> getRoles() {
        return new ArrayList<>(roles.values());
    }

    public static Role getById(int id) {
        return roles.get(id);
    }

    @Override
    public Class getTargetClass() {
        return Role.class;
    }

    @Override
    public List<?> getData() {
        return getRoles();
    }

    @Override
    public void setData(List<Object> data) {
        data.forEach(item -> {
            Role role = (Role) item;
            roles.replace(role.getDomainId(), role);
        });
    }

}

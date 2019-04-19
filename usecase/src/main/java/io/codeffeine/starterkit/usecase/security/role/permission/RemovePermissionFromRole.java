/*
 * StarterKit.
 */
package io.codeffeine.starterkit.usecase.security.role.permission;

import io.codeffeine.starterkit.usecase.exception.data.DataNotFoundException;
import io.codeffeine.starterkit.domain.security.contract.method.GetSecureMethodInterface;
import io.codeffeine.starterkit.domain.security.contract.role.GetRoleInterface;
import io.codeffeine.starterkit.domain.security.contract.role.permission.RemovePermissionFromRoleInterface;
import io.codeffeine.starterkit.domain.security.entity.Role;
import io.codeffeine.starterkit.domain.security.entity.SecureMethod;
import io.codeffeine.starterkit.domain.security.repository.RoleRepositoryInterface;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Mirko Gueregat @mgueregath <mgueregath@codeffeine.io>
 */
public class RemovePermissionFromRole implements RemovePermissionFromRoleInterface {

    private final RoleRepositoryInterface repository;
    private final GetRoleInterface getRole;
    private final GetSecureMethodInterface getSecureMethod;

    @Inject
    public RemovePermissionFromRole(
            RoleRepositoryInterface repository,
            GetRoleInterface getRole,
            GetSecureMethodInterface getSecureMethod
    ) {
        this.repository = repository;
        this.getRole = getRole;
        this.getSecureMethod = getSecureMethod;
    }

    @Override
    public boolean remove(int roleId, int serviceId, int methodId) {
        Role role = getRole.getById(roleId);
        SecureMethod method = getSecureMethod.getByServiceAndMethod(serviceId, methodId);
        Set<SecureMethod> methods = new HashSet<>();
        for (SecureMethod m : role.getPermissions()) {
            if (m.getMethod().getService() != serviceId || m.getMethod().getMethod() != methodId) {
                methods.add(m);
            }
        }
        if (role.getPermissions().size() == methods.size()) {
            throw new DataNotFoundException();
        }

        role.setPermissions(methods);
        role = repository.persist(role);
        return !role.getPermissions().contains(method);
    }

}

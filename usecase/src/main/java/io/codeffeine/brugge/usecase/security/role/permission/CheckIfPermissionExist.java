/*
 * Brugge Framework.
 */
package io.codeffeine.brugge.usecase.security.role.permission;

import io.codeffeine.brugge.domain.security.contract.role.GetRoleInterface;
import io.codeffeine.brugge.domain.security.contract.role.permission.CheckIfPermissionExistInterface;
import io.codeffeine.brugge.domain.security.entity.Role;
import io.codeffeine.brugge.domain.security.repository.RoleRepositoryInterface;
import com.google.inject.Inject;

/**
 *
 * @author Mirko Gueregat @mgueregath <mgueregath@codeffeine.io>
 */
public class CheckIfPermissionExist implements CheckIfPermissionExistInterface {

    private final GetRoleInterface getRole;
    private final RoleRepositoryInterface repository;

    @Inject
    public CheckIfPermissionExist(
            RoleRepositoryInterface repository,
            GetRoleInterface getRole
    ) {
        this.getRole = getRole;
        this.repository = repository;
    }

    @Override
    public boolean check(int roleId, int serviceId, int methodId) {
        Role role = getRole.getById(roleId);
        return repository.checkIfRoleHasPermission(role, serviceId, methodId);
    }

}

/*
 * StarterKit.
 */
package cl.emendare.starterkit.facade.permission;

import cl.emendare.starterkit.domain.security.contract.method.NewSecureMethodInterface;
import cl.emendare.starterkit.domain.security.contract.role.permission.AddPermissionToRoleInterface;
import cl.emendare.starterkit.domain.security.contract.role.permission.CheckIfPermissionExistInterface;
import cl.emendare.starterkit.domain.security.entity.SecureMethod;
import cl.emendare.starterkit.facade.permission.annotation.Permission;
import cl.emendare.starterkit.facade.permission.annotation.ProtectedService;
import cl.emendare.starterkit.usecase.keeper.CounterKeeper;
import cl.emendare.starterkit.domain.migration.data.Roles;
import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

/**
 *
 * @author Mirko Gueregat @mgueregath <mgueregath@emendare.cl>
 */
public class ServiceRegister {

    @Inject
    private NewSecureMethodInterface newSecureMethod;

    @Inject
    private AddPermissionToRoleInterface addPermissionToRole;

    @Inject
    private CheckIfPermissionExistInterface checkIfPermissionExist;

    private static final List<Integer> services = new ArrayList<>();
    private static boolean areRegistered = false;

    public void registerServices() {
        if (areRegistered) {
            return;
        }

        CounterKeeper.setServices(ServiceIdentification.getMaxService());

        Set<Class<?>> protectedServices = new Reflections("cl.emendare.starterkit.facade.service").getTypesAnnotatedWith(ProtectedService.class);
        for (Class<?> service : protectedServices) {
            registerFunctions(service);
        }
        areRegistered = true;
    }

    private void registerFunctions(Class<?> target) {
        ProtectedService service = (ProtectedService) target.getAnnotation(ProtectedService.class);
        if (!services.contains(service.service())) {
            Method[] methods = target.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(Permission.class) != null) {
                    SecureMethod sm = newSecureMethod.add(
                            service.service(),
                            method.getAnnotation(Permission.class).method(),
                            method.getName(),
                            method.getAnnotation(Permission.class).details()
                    );
                    int roles[] = method.getAnnotation(Permission.class).roles();
                    for (int i = 0; i < roles.length; i++) {
                        if (!checkIfPermissionExist.check(Roles.getById(roles[i]).getId(), service.service(), method.getAnnotation(Permission.class).method())) {
                            addPermissionToRole.add(
                                    Roles.getById(roles[i]).getId(),
                                    service.service(),
                                    method.getAnnotation(Permission.class).method()
                            );
                        }

                    }
                    CounterKeeper.setMethods(method.getAnnotation(Permission.class).method());
                }
            }
            services.add(service.service());
        }

    }
}

package hierarchical.authorities.demo.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@Component
public class HierarchicalSecuredAspect {

    @Around("methodsAnnotatedWithHierarchicalSecuredAnnotation()")
    public Object processMethodsAnnotatedWithHierarchicalSecuredAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HierarchicalSecured hierarchicalSecuredAnnotation = method.getAnnotation(HierarchicalSecured.class);

        String[] shouldHaveAuthorities = hierarchicalSecuredAnnotation.value();

        Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // check if the user has any of the authorities or dependent authorities
        for (String shouldHaveAuthority : shouldHaveAuthorities) {
            for (GrantedAuthority userGrantedAuthority : userAuthorities) {
                String userAuthority = userGrantedAuthority.getAuthority();
                if (userAuthority.equals(shouldHaveAuthority) || AuthoritiesDependencies.isDependent(userAuthority, shouldHaveAuthority)) {
                    return joinPoint.proceed();
                }
            }
        }
        throw new AccessDeniedException("dour 100");
    }

    @Pointcut("@annotation(hierarchical.authorities.demo.security.HierarchicalSecured)")
    private void methodsAnnotatedWithHierarchicalSecuredAnnotation() {

    }
}

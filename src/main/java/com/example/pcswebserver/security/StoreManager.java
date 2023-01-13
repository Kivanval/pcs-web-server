package com.example.pcswebserver.security;

import com.example.pcswebserver.domain.Role;
import com.example.pcswebserver.domain.StorePermissionType;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.example.pcswebserver.domain.StorePermissionType.MODIFY;
import static com.example.pcswebserver.domain.StorePermissionType.READ;
import static com.example.pcswebserver.web.WebConstants.STORE;

@Component
public class StoreManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        var authentication = authenticationSupplier.get();
        if (authentication == null) return new AuthorizationDecision(false);

        var requestURI = context.getRequest().getRequestURI();

        if (requestURI.matches(STORE + "/[^/]+/[^/]+")) return new AuthorizationDecision(true);

        return new AuthorizationDecision(
                switch (RequestMethod.valueOf(context.getRequest().getMethod())) {
                    case GET -> hasAccess(requestURI, READ, authentication);
                    case POST, PUT, PATCH, DELETE -> hasAccess(requestURI, MODIFY, authentication);
                    default -> false;
                });
    }

    private boolean hasAccess(String requestURI, StorePermissionType permissionType, Authentication authentication) {
        var srcUrl = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        return isAdmin(authentication) || hasAccessToSrc(srcUrl, permissionType, authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.startsWith("ROLE") && auth.endsWith(Role.ADMIN));
    }

    private boolean hasAccessToSrc(String srcUrl, StorePermissionType permissionType, Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Predicate.not(auth -> auth.startsWith("ROLE")))
                .filter(auth -> auth.endsWith(srcUrl))
                .anyMatch(auth -> {
                    var splitted = auth.split("_");
                    if (splitted[0].equals(permissionType.toString())) return true;
                    return hasParentAccess(permissionType, splitted[1]) || (splitted.length == 3
                            && hasParentAccess(permissionType, splitted[2]));
                });
    }

    private boolean hasParentAccess(StorePermissionType permissionType, String srcUrl) {
        return StorePermissionType.valueOf(srcUrl)
                .getChildren()
                .stream()
                .anyMatch(authPermissionType -> authPermissionType == permissionType);
    }
}

package com.example.pcswebserver.security;

import com.example.pcswebserver.domain.StorePermissionType;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.example.pcswebserver.domain.StorePermissionType.MODIFY;
import static com.example.pcswebserver.domain.StorePermissionType.READ;
import static com.example.pcswebserver.web.WebConstants.STORE;

public class StoreManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        var authentication = authenticationSupplier.get();
        if (authentication == null) return new AuthorizationDecision(false);

        var requestURI = context.getRequest().getRequestURI();

        if (requestURI.matches(STORE + "/[^/]+/[^/]+")) return new AuthorizationDecision(true);

        var srcUrl = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        return new AuthorizationDecision(
                switch (RequestMethod.valueOf(context.getRequest().getMethod())) {
                    case GET -> isAdmin(authentication) || hasAccessToSrc(srcUrl, READ, authentication);
                    case POST, PUT, PATCH, DELETE ->
                            isAdmin(authentication) || hasAccessToSrc(srcUrl, MODIFY, authentication);
                    default -> false;
                });
    }

    private boolean hasAccessToSrc(String srcUrl, StorePermissionType permissionType, Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Predicate.not(auth -> auth.startsWith("ROLE")))
                .filter(auth -> auth.endsWith(srcUrl))
                .anyMatch(auth -> {
                    if (auth.startsWith(permissionType.toString())) return true;
                    return StorePermissionType.valueOf(auth.substring(0, auth.indexOf('_')))
                            .getChildren()
                            .stream()
                            .anyMatch(authPermissionType -> authPermissionType == permissionType);
                });
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.startsWith("ROLE") && auth.endsWith("ADMIN"));
    }
}

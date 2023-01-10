package com.example.pcswebserver.security;

import com.example.pcswebserver.domain.StorePermissionType;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.example.pcswebserver.domain.StorePermissionType.MODIFY;
import static com.example.pcswebserver.domain.StorePermissionType.READ;

public class StoreManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        var authentication = authenticationSupplier.get();
        String requestURI = context.getRequest().getRequestURI();
        var srcUrl = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        return new AuthorizationDecision(
                switch (RequestMethod.valueOf(context.getRequest().getMethod())) {
                    case GET -> mustBePermission(srcUrl, READ, authentication);
                    case POST, PUT, PATCH, DELETE -> mustBePermission(srcUrl, MODIFY, authentication);
                    default -> false;
                });
    }

    private boolean mustBePermission(String srcUrl, StorePermissionType permissionType, Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> {
                    var matcher = Pattern.compile("([^_]+)").matcher(auth);
                    if (matcher.groupCount() != 2) return false;
                    var permissionAuth = matcher.group(0);
                    var srcAuth = matcher.group(1);
                    if (!srcUrl.equals(srcAuth)) return false;
                    if (permissionAuth.equals(permissionType.toString())) return true;
                    return permissionType
                            .getChildren()
                            .stream()
                            .map(StorePermissionType::toString)
                            .anyMatch(permission -> permission.equals(permissionAuth));
                });
    }
}

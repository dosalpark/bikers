//package org.example.bikers.global.security;
//
//import java.util.Collection;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class CustomAuthentication implements Authentication {
//
//    private final UserDetails userDetails;
//    private boolean isAuthenticated;
//
//
//    public CustomAuthentication(UserDetails userDetails) {
//        this.isAuthenticated = true;
//        this.userDetails = userDetails;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getDetails() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.userDetails;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return this.isAuthenticated;
//    }
//
//    @Override
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//        this.isAuthenticated = isAuthenticated;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//}

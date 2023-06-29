package favorite.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

public class AppUser implements UserDetails {
    private BigInteger appUserId;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final String password;
    private final LocalDate registeredOn;
    private final LocalDate lastLogin;
    private boolean userEnabled;
    private final Collection<GrantedAuthority> authorities;

    public AppUser(BigInteger appUserId, String firstName, String middleName, String lastName, String phone, String email, String password, LocalDate registeredOn, LocalDate lastLogin, boolean userEnabled, List<String> roles) {
        this.appUserId = appUserId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.registeredOn = registeredOn;
        this.lastLogin = lastLogin;
        this.userEnabled = userEnabled;
        this.authorities = convertRolesToAuthorities(roles);
    }

    private static Collection<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public BigInteger getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(BigInteger appUserId) {
        this.appUserId = appUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    @Override
    public boolean isEnabled() {
        return userEnabled;
    }

    public void setEnabled(boolean userEnabled) {
        this.userEnabled = userEnabled;
    }
}
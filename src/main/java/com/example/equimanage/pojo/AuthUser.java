package com.example.equimanage.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class AuthUser extends User implements UserDetails{
    //private User user;

    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(User user, String groupName) {
        super(user);
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(groupName));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    // 账号是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 账号凭证是否未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账号是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }

}

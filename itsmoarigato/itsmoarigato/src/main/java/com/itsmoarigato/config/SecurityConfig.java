package com.itsmoarigato.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.itsmoarigato.User;
import com.itsmoarigato.model.UserManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserManager userManager;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(new UserDetailsServiceImpl())
			.passwordEncoder(new StandardPasswordEncoder());
//4Test			.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	super.configure(http);
    	http.headers().frameOptions().disable();
        //http.csrf().disable();

    }

	private class UserDetailsServiceImpl implements UserDetailsService	{
		@Override
		public UserDetails loadUserByUsername(String username)
				throws UsernameNotFoundException {
			
			return toUserDetails(userManager.getUser(username,username));
		}

		private UserDetails toUserDetails(final User user) {
			return new UserDetails() {
				private static final long serialVersionUID = -977449495212347071L;

				@Override
				public boolean isEnabled() {
					return true;
				}
				
				@Override
				public boolean isCredentialsNonExpired() {
					return true;
				}
				
				@Override
				public boolean isAccountNonLocked() {
					return true;
				}
				
				@Override
				public boolean isAccountNonExpired() {
					return true;
				}
				
				@Override
				public String getUsername() {
					return user.getEmail();
				}
				
				@SuppressWarnings("unused")
				public User getUser(){
					return user;
				}
				
				@Override
				public String getPassword() {
					return user.getPassword();
				}
				
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					return new ArrayList<>();
				}
			};
		}
	}
}


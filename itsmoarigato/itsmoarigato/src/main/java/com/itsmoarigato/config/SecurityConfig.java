package com.itsmoarigato.config;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	JdbcTemplate jdbcTemplate;


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
			
			return jdbcTemplate.queryForObject("select email,name,password from user_Tbl where email = ?", new RowMapper_(),new Object[]{username});
		}
	}
	
	private static class RowMapper_ implements RowMapper<UserDetails>,Serializable{
		
		public RowMapper_() {
			super();
		}

		@Override
		public UserDetails mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			
			
			final String username = rs.getString("email");
			final String name = rs.getString("name");
			final String password = rs.getString("password");

			return new UserDetails() {
				
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
					return username;
				}
				
				public String getName() {
					return name;
				}
				
				@Override
				public String getPassword() {
					return password;
				}
				
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					return new ArrayList<>();
				}
			};
		}
	}
}


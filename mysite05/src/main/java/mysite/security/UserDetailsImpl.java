package mysite.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mysite.vo.UserVo;

@SuppressWarnings("serial")
public class UserDetailsImpl extends UserVo implements UserDetails {

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	
}

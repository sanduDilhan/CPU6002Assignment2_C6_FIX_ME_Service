package com.sentura.bLow.configuration;

import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.exception.types.UserNotActiveException;
import com.sentura.bLow.exception.types.UserNotVerifiedException;
import com.sentura.bLow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthService authService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, UserNotVerifiedException, UserNotActiveException {
		UserDetail user = authService.findUserByEmail(email);
		if (user != null && !user.getIsActive()) throw new UserNotActiveException();
//		if (user != null && !user.getVerified()) throw new UserNotVerifiedException();
		if (user != null) {
			List<GrantedAuthority> listAuthorities = new ArrayList<>();
			listAuthorities.add(new Role(user.getUserRole()));
			return new User(email, user.getPassword(), listAuthorities);
		} else {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
	}

}
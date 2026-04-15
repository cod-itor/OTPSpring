package com.example.javamailsender.Service.impl;
import com.example.javamailsender.Service.AppUserService;
import com.example.javamailsender.model.Request.AppUserRequest;
import com.example.javamailsender.model.Response.AppUserResponse;
import com.example.javamailsender.model.entity.AppUser;
import com.example.javamailsender.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.getUserByEmail(email);
    }

    @Override
    public AppUserResponse register(AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        AppUser appUser = appUserRepository.register(request);
        return modelMapper.map(appUserRepository.getUserById(appUser.getUserId()), AppUserResponse.class);
    }

}

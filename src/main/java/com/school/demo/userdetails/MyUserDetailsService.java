package com.school.demo.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.school.demo.mapper.UserMapper;
import com.school.demo.object.UserVo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo member = userMapper.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }
        
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole())
                .build();
    }
}

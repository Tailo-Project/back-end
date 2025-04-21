package com.growith.tailo.security.jwt.service;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        return this.memberRepository.findByAccountId(accountId).orElseThrow(()->new ResourceNotFoundException("AccountId가 없습니다."));
    }
}

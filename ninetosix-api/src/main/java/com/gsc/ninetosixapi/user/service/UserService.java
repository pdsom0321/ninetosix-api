package com.gsc.ninetosixapi.user.service;

import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.company.repository.CompanyRepository;
import com.gsc.ninetosixapi.company.service.CompanyService;
import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.entity.UserRole;
import com.gsc.ninetosixapi.user.repository.UserRepository;
import com.gsc.ninetosixapi.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final CompanyService companyService;

    private final PasswordEncoder passwordEncoder;

    public void create(UserInfoDTO userInfoDTO) {
        if(userRepository.findByEmail(userInfoDTO.getEmail()).isEmpty()) {
            Company company = companyService.getCompany(userInfoDTO.getCompanyCode());

            User user = userRepository.save(User.createUser(userInfoDTO, passwordEncoder, company));
            userRoleRepository.save(UserRole.createUserRole(user));
        } else {
            throw new DuplicateKeyException("중복된 이메일 입니다.");
        }
    }

}

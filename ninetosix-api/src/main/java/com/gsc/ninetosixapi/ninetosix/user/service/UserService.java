package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.entity.UserRole;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final CompanyService companyService;

    public void create(UserInfoDTO userInfoDTO) {
        if(userRepository.findByEmail(userInfoDTO.getEmail()).isEmpty()) {
            Company company = companyService.getCompany(userInfoDTO.getCompanyCode());
            User user = userRepository.save(User.createUser(userInfoDTO, company));
            userRoleRepository.save(UserRole.createUserRole(user));
        } else {
            throw new DuplicateKeyException("중복된 이메일 입니다.");
        }
    }

}

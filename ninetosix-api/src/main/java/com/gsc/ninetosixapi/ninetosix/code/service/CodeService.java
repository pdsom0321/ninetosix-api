package com.gsc.ninetosixapi.ninetosix.code.service;

import com.gsc.ninetosixapi.ninetosix.code.dto.CodesResDTO;
import com.gsc.ninetosixapi.ninetosix.code.repository.CodeRepository;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CodeService {
    private final CodeRepository codeRepository;

    public List<CodesResDTO> getCodes(String codeGroup) {
        return codeRepository.findAllByCodeGroupAndUseYn(codeGroup, YNCode.Y).stream()
                .map(CodesResDTO::getCodes)
                .collect(Collectors.toList());
    }
}

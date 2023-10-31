package com.gsc.ninetosixapi.ninetosix.signature.service;

import com.gsc.ninetosixapi.ninetosix.signature.entity.Signature;
import com.gsc.ninetosixapi.ninetosix.signature.repository.SignatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignatureService {
    private final SignatureRepository signatureRepository;

    public void createSignature(long memberId, String signature) {
        Optional<Signature> existingSignature = signatureRepository.findByMemberId(memberId);

        if (existingSignature.isPresent()) {
            deleteSignature(memberId);
        }
        signatureRepository.save(Signature.create(signature, memberId));
    }

    @Transactional(readOnly = true)
    public String signature(Long memberId) {
        return signatureRepository.findByMemberId(memberId).orElseThrow(EntityNotFoundException::new).getSignature();
    }

    public void deleteSignature(Long memberId) {
        signatureRepository.deleteByMemberId(memberId);
    }
}

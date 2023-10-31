package com.gsc.ninetosixapi.ninetosix.signature.controller;

import com.gsc.ninetosixapi.core.aspect.UserId;
import com.gsc.ninetosixapi.ninetosix.signature.dto.SignatureDTO;
import com.gsc.ninetosixapi.ninetosix.signature.service.SignatureService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class SignatureController {
    private final SignatureService signatureService;

    @UserId
    @ApiOperation(value = "내 서명 등록")
    @PostMapping("signature")
    public ResponseEntity<Void> createSignature(HttpServletRequest request, @RequestBody SignatureDTO reqDTO) {
        long memberId = (long) request.getAttribute("memberId");
        signatureService.createSignature(memberId, reqDTO.signature());
        return ResponseEntity.ok().build();
    }

    @UserId
    @ApiOperation(value = "내 서명 조회")
    @GetMapping("signature")
    public ResponseEntity<String> signature(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        return ResponseEntity.ok(signatureService.signature(memberId));
    }

    @UserId
    @ApiOperation(value = "내 서명 삭제")
    @DeleteMapping("signature")
    public ResponseEntity<Void> deleteSignature(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        signatureService.deleteSignature(memberId);
        return ResponseEntity.ok().build();
    }
}

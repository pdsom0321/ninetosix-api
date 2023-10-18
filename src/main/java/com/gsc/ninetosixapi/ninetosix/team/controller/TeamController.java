package com.gsc.ninetosixapi.ninetosix.team.controller;

import com.gsc.ninetosixapi.ninetosix.team.dto.TeamsResDTO;
import com.gsc.ninetosixapi.ninetosix.team.service.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @ApiOperation(value = "회사의 팀 목록")
    @GetMapping("teams/{companyId}")
    public ResponseEntity<List<TeamsResDTO>> teams(@PathVariable long companyId) {
        return ResponseEntity.ok(teamService.teams(companyId));
    }

}

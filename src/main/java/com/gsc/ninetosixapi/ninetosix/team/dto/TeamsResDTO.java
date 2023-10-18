package com.gsc.ninetosixapi.ninetosix.team.dto;

import com.gsc.ninetosixapi.ninetosix.team.entity.Team;

public record TeamsResDTO(long id, String name) {
    public static TeamsResDTO of(Team team) {
        return new TeamsResDTO(team.getId(), team.getName());
    }
}

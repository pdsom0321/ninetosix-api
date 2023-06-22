package com.gsc.ninetosixapi.ninetosix.attend.dto;

import java.util.List;

public record ExportDTO(String memberName, List<AttendDTO> attends) {}

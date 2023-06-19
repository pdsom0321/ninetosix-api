package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.core.aspect.UserId;
import com.gsc.ninetosixapi.ninetosix.attend.dto.*;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    /**
     * ResponseEntity 제네릭 타입: ResponseEntity의 제네릭 타입을 Void로 변경하여 명시적으로 응답 본문이 없음을 나타냅니다. 이는 클라이언트에게 빈 본문을 반환하는 것과 동일합니다.
     * ResponseEntity.ok().build(): ResponseEntity의 ok() 메서드를 사용하여 HTTP 상태 코드를 200 OK로 설정하고, build() 메서드를 호출하여 ResponseEntity 객체를 생성합니다. 이를 통해 간결하게 응답을 생성할 수 있습니다.@return
     */
    @ApiOperation(value = "출근", notes = "attendCode, locationCode -> insert Attend 실행")
    @PostMapping("attend/on")
    public ResponseEntity<Void> onWork(@RequestBody OnWorkReqDTO reqDTO) {
        attendService.onWork(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "출근", notes = "요청: locationCode / 실행: update inTime, locationCode")
    @PutMapping("attend/on")
    public ResponseEntity<Void> onWorkDuringDayOff(@RequestBody OnWorkDuringDayOffReqDTO reqDTO) {
        attendService.onWorkDuringDayOff(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "퇴근", notes = "실행: update outTime")
    @PostMapping("attend/off")
    public ResponseEntity offWork() {
        attendService.offWork();
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "휴가 및 그외 신청", notes = "attendCode, fromDate, toDate -> insert Attend 실행")
    @PostMapping("attend/{attendCode}")
    public ResponseEntity dayOff(@PathVariable String attendCode, @RequestBody AttendCodeReqDTO reqDTO) {
        attendService.dayOff(attendCode, reqDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @UserId
    @GetMapping("attend")
    public ResponseEntity<AttendResDTO> attendInfo(HttpServletRequest request){
        Long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(attendService.attendInfo(memberId));
    }
    /*public ResponseEntity<List<AttendResDTO>> yesterdayAndTodayAttendanceList(HttpServletRequest request){
        Long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(attendService.yesterdayAndTodayAttendanceList(memberId));
    }*/

    @UserId
    @GetMapping("attend/{month}")
    public ResponseEntity<List<MonthlyResDTO>> monthlyAttendanceList(HttpServletRequest request, @PathVariable String month){
        Long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(attendService.monthlyAttendanceList(memberId, month));
    }
}

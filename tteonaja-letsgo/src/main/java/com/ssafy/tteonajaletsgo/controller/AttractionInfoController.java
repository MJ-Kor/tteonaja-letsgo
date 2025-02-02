package com.ssafy.tteonajaletsgo.controller;

import com.ssafy.tteonajaletsgo.domain.AttractionInfo;
import com.ssafy.tteonajaletsgo.domain.AttractionReview;
import com.ssafy.tteonajaletsgo.domain.Gugun;
import com.ssafy.tteonajaletsgo.domain.Sido;
import com.ssafy.tteonajaletsgo.dto.AttractionInfoAndDescription;
import com.ssafy.tteonajaletsgo.dto.attractionInfo.AttractionInfoCheckDto;
import com.ssafy.tteonajaletsgo.dto.attractionReview.AttractionReviewCheckDto;
import com.ssafy.tteonajaletsgo.dto.common.CheckDto;
import com.ssafy.tteonajaletsgo.exception.ExceptionResponse;
import com.ssafy.tteonajaletsgo.service.AttractionInfoService;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("tteonaja/api/attractionInfo/")
@RequiredArgsConstructor
public class AttractionInfoController {

    private final AttractionInfoService attractionInfoService;


    @Operation(summary = "조건에 맞는 여행지 정보 불러오기", description = "여행지 정보를 불러온다.")
    @GetMapping("/region")
//    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public List<AttractionInfoAndDescription> getRegion(@RequestParam Map<String, String> map) throws SQLException {
        log.debug("searchTerm = {}", map.get("searchTerm"));
        List<AttractionInfoAndDescription> list = attractionInfoService.getRegion(map);
        return list;
    }
    @Operation(summary = "추가적인 관광지 정보 얻어오기", description = "추가적인 여행지 정보를 불러운다(20개씩)")
    @GetMapping("/region/additional")
    public List<AttractionInfoAndDescription> getAdditionalRegion(@RequestParam Map<String, Object> map) throws SQLException {
        log.debug("searchTerm = {}", map.get("searchTerm"));

        List<AttractionInfoAndDescription> list = attractionInfoService.getAdditionalRegion(map);
        return list;
    }

    @Operation(summary = "id로 관광지 정보 얻어오기", description = "아이디당 여행정보 좋아요 및 좋아요 취소를 한다.")
    @GetMapping("/attraction/{id}")
    public ResponseEntity<?> getAttraction(@PathVariable(value = "id", required = true) int id) {
        try {
            return new ResponseEntity<AttractionInfoAndDescription>(attractionInfoService.getAttraction(id), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionResponse.response(e);
        }
    }

    @Operation(summary = "시/도 정보 불러오기", description = "시/도 정보를 불러온다.")
    @GetMapping("/sido")
    public List<Sido> getSido() throws SQLException {

        List<Sido> list = attractionInfoService.getSido();
        return list;
    }

    @Operation(summary = "구군 정보 불러오기", description = "구군 정보를 불러운다.")
    @GetMapping("/gugun/{sidoCode}")
    public List<Gugun> getGugun(@PathVariable("sidoCode") int sidoCode) throws SQLException {
        List<Gugun> list = attractionInfoService.getGugun(sidoCode);
        return list;
    }

    @Operation(summary = "여행지 탑 5", description = "여행지 탑 5를 불러온다.")
    @GetMapping("/top")
    public ResponseEntity<?> getTopAttraction() {

        try {
            List<AttractionInfoAndDescription> topAttraction = attractionInfoService.getTopAttraction();

            if (topAttraction != null) {
                return new ResponseEntity<List<AttractionInfoAndDescription>>(topAttraction, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.OK);
            }

        } catch (Exception e) {
            return ExceptionResponse.response(e);
        }
    }

    @Operation(summary = "여행정보 좋아요", description = "아이디당 여행정보 좋아요 및 좋아요 취소를 한다.")
    @GetMapping("/like/{id}/{userid}")
    public ResponseEntity<?> likeReview(@PathVariable(value = "id", required = true) int id,
                                        @PathVariable(value = "userid", required = true) String userid) {

        try {
            AttractionInfoCheckDto attractionInfoCheckDto = new AttractionInfoCheckDto();
            attractionInfoCheckDto.setId(id);
            attractionInfoCheckDto.setUserId(userid);
            CheckDto checkDto = new CheckDto();
            if (attractionInfoService.checkInfo(attractionInfoCheckDto)) {
                attractionInfoService.likeCancelInfo(id);
                attractionInfoService.deleteCheckInfo(attractionInfoCheckDto);
                checkDto.setCheck(true);
            } else {
                attractionInfoService.likeInfo(id);
                attractionInfoService.insertCheckInfo(attractionInfoCheckDto);
                checkDto.setCheck(false);
            }
            return new ResponseEntity<CheckDto>(checkDto, HttpStatus.OK);
        } catch (Exception e) {
            log.debug("reviewLikeError = {}", e);
            return ExceptionResponse.response(e);
        }
    }

}

package com.ssafy.tteonajaletsgo.dto.travelCourse;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class TravelCourseSaveDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
    @NotBlank
    private String travelList;
}

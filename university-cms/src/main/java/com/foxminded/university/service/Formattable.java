package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.web.dto.LectureDto;

public interface Formattable {

    List<LectureDto> getStandardFormat(List<Lecture> lectures);

}

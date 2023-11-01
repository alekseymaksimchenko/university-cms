package com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.Formattable;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.impl.DateParser;
import com.foxminded.university.web.dto.LectureDto;

@Controller
public class LectureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LectureController.class);
    private static final String HAS_ANY_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')";
    private static final String HAS_ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    private List<String> lectureTimeOptions;

    private final LectureService lectureService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final DateParser dateParser;
    private final Formattable formattable;

    public LectureController(LectureService lectureService, CourseService courseService, GroupService groupService,
            DateParser dateParser, Formattable formattable) {
        super();
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.dateParser = dateParser;
        this.formattable = formattable;
    }

    @PostConstruct
    private void initLectureTimeOptions() {
        lectureTimeOptions = new ArrayList<>();
        lectureTimeOptions.add("09:00-11:20");
        lectureTimeOptions.add("11:30-12:50");
        lectureTimeOptions.add("13:30-14:50");
        lectureTimeOptions.add("15:00-16:20");
    }

    @ModelAttribute("lectureDto")
    public LectureDto lectureDto() {
        return new LectureDto();
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/lecture")
    public String showTodayLectures(Model model) {
        String currentDate = dateParser.getCurrentDateAsString(LocalDate.now());
        List<LectureDto> lecturesToday = formattable
                .getStandardFormat(lectureService.findAllLecturesOnDateOrderByDateAscTimeAsc(currentDate));
        model.addAttribute("lecturesToday", lecturesToday);
        LOGGER.trace("Lecture Today list size is {} according to {} currentDate.", lecturesToday.size(), currentDate);
        return "lecture";
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/lecture/week")
    public String showWeekLectures(Model model) {
        String startDate = dateParser.getCurrentDateAsString(LocalDate.now());
        int daysTillEndOfWeek = dateParser.getDaysUntilEndOfWeekAsInt(LocalDate.now());
        String endDate = dateParser.getCurrentDateAsString(LocalDate.now().plusDays(daysTillEndOfWeek));

        List<LectureDto> lecturesWeek = formattable
                .getStandardFormat(lectureService.findAllLecturesBetweenDatesOrderByDateAscTimeAsc(startDate, endDate));
        model.addAttribute("lecturesWeek", lecturesWeek);
        LOGGER.trace("Lecture Week list size is {} according to {} daysTillEndOfWeek.", lecturesWeek.size(),
                daysTillEndOfWeek);
        return "lecture-show-week";
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/lecture/month")
    public String showMonthLectures(Model model) {
        String startDate = dateParser.getCurrentDateAsString(LocalDate.now());
        int daysTillEndOfMonth = dateParser.getDaysUntilEndOfMonthAsInt(LocalDate.now());
        String endDate = dateParser.getCurrentDateAsString(LocalDate.now().plusDays(daysTillEndOfMonth));
        List<LectureDto> lecturesMonth = formattable
                .getStandardFormat(lectureService.findAllLecturesBetweenDatesOrderByDateAscTimeAsc(startDate, endDate));

        model.addAttribute("lecturesMonth", lecturesMonth);
        LOGGER.trace("Lecture Month list size is {} according to {} daysTillEndOfMonth.", lecturesMonth.size(),
                daysTillEndOfMonth);
        return "lecture-show-month";
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/lecture/all")
    public String showAllLectures(Model model) {
        List<Lecture> allLectures = lectureService.findAllLecturesOrderByDateAscTimeAsc();
        model.addAttribute("allLectures", allLectures);
        LOGGER.trace("Lecture all list size is {}", allLectures.size());
        return "lecture-show-all";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/lecture/new")
    public String createLectureForm(Model model) {
        Lecture newLecture = new Lecture();
        model.addAttribute("lecture", newLecture);
        model.addAttribute("lectureTimeOptions", lectureTimeOptions);
        return "lecture-create";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("/lecture")
    public String createLecture(@ModelAttribute("lectureDto") LectureDto lectureDto) {
        Lecture newLecture = new Lecture();
        LOGGER.trace("New lectureDto is {}", lectureDto);

        newLecture.setLectureDate(lectureDto.getLectureDate());
        newLecture.setLectureTime(lectureDto.getLectureTime());

        Long courseId = Long.parseLong(lectureDto.getCourseId());
        Course existingCourse = courseService.getCourseById(courseId);
        newLecture.setCourse(existingCourse);
        existingCourse.addLectureToCourse(newLecture);
        courseService.updateCourse(existingCourse, courseId);

        Long groupdId = Long.parseLong(lectureDto.getGroupId());
        Group existingGroup = groupService.getGroupById(groupdId);
        newLecture.setGroup(existingGroup);
        existingGroup.addLectureToGroup(newLecture);
        groupService.updateGroup(existingGroup, groupdId);

        LOGGER.debug("New lectureModel is {}", newLecture);
        lectureService.saveLecture(newLecture);
        return "redirect:/lecture";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("lecture/update/{id}")
    public String updateLectureForm(@PathVariable Long id, Model model) {
        model.addAttribute("lecture", lectureService.getLectureById(id));
        model.addAttribute("lectureTimeOptions", lectureTimeOptions);
        return "lecture-update";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("lecture/{id}")
    public String updateLecture(@PathVariable Long id, @ModelAttribute("lecture") LectureDto lectureDto) {
        Lecture existingLecture = lectureService.getLectureById(id);
        existingLecture.setLectureDate(lectureDto.getLectureDate());
        existingLecture.setLectureTime(lectureDto.getLectureTime());

        Long courseId = Long.parseLong(lectureDto.getCourseId());
        Course existingCourse = courseService.getCourseById(courseId);
        existingLecture.setCourse(existingCourse);

        Long groupId = Long.parseLong(lectureDto.getGroupId());
        Group existingGroup = groupService.getGroupById(groupId);
        existingLecture.setGroup(existingGroup);

        lectureService.updateLecture(existingLecture, id);

        return "redirect:/lecture";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/lecture/delete/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.deleteLecture(id);
        return "redirect:/lecture";
    }

}

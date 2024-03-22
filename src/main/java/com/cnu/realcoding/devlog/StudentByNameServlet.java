package com.cnu.realcoding.devlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// GET
// 전체 학생 조회 : /students
// 이름으로 특정 학생 조회 : /students/{name}
// 학년으로 n명의 학생 조회 : /students?grade={grade}

@WebServlet(name = "studentServlet", value = "/students/*")
public class StudentByNameServlet extends HttpServlet {
    record Student(String name, Integer grade) {}
    List<Student> students = List.of(
            new Student("jisoo", 1),
            new Student("jennie", 2),
            new Student("rose", 2),
            new Student("lisa", 4)
    );
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getPathInfo();
        if (name == null) {
            String gardeParam = request.getParameter("grade");
            if (gardeParam == null) {
                getAllStudents(response);
            } else {
                Integer grade = Integer.parseInt(gardeParam);
                getStudentsByGrade(grade, response);
            }
        } else {
            getStudentByName(name, response);
        }
    }

    private void getStudentsByGrade(Integer grade, HttpServletResponse response) throws IOException {
        List<Student> studentsByGrade = students.stream()
                .filter(it -> Objects.equals(it.grade(), grade))
                .toList();

        if (studentsByGrade.isEmpty()) {
            response.setStatus(404);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(studentsByGrade);
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print(responseJson);
        }
    }

    private void getAllStudents(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(students);

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(responseJson);
    }

    private void getStudentByName(String name, HttpServletResponse response) throws IOException {
        Optional<Student> student = students.stream()
                .filter(it -> it.name().equals(name.substring(1)))
                .findFirst();
        if (student.isEmpty()) {
            response.setStatus(404);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(student.get());
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print(responseJson);
        }
    }
}
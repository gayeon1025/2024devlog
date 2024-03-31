package com.cnu.realcoding.devlog_servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// GET
// 전체 학생 조회 : /students
// 이름으로 특정 학생 조회 : /students/{name}
// 학년으로 n명의 학생 조회 : /students?grade={grade}
public class StudentServlet extends HttpServlet {
    record Student(String name, Integer grade) {}

    // DB 대신 List로 대체
    List<Student> students = new ArrayList<>(
            List.of(
                    new Student("jisoo", 1),
                    new Student("jennie", 2),
                    new Student("rose", 2),
                    new Student("lisa", 4)
            )
    );

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getPathInfo();
        if (name == null) {
            getAllStudents(response);
        } else {
            getStudentByName(name, response);
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

        ObjectMapper objectMapper = new ObjectMapper();
        if (student.isEmpty()) {
            response.setStatus(404);
            response.setContentType("application/json");
            record ErrorResponse(String error) {}
            ErrorResponse errorResponse = new ErrorResponse("존재하지 않는 학생입니다.");
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().print(errorJson);
        } else {

            String responseJson = objectMapper.writeValueAsString(student.get());
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print(responseJson);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String requestJson = parseRequestBody(request);
            ObjectMapper objectMapper = new ObjectMapper();
            Student student = objectMapper.readValue(requestJson, Student.class);

            students.add(student);

            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print(objectMapper.writeValueAsString(student));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}

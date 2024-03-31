package com.cnu.realcoding.devlog_servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(students);

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(responseJson);
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

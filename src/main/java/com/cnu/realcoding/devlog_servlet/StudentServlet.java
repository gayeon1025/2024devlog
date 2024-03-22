//package com.cnu.realcoding.devlog;
//
//import java.io.*;
//import java.util.List;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//// GET
//// 전체 학생 조회 : /students
//// 이름으로 특정 학생 조회 : /students/{name}
//// 학년으로 n명의 학생 조회 : /students?grade={grade}
//
//@WebServlet(name = "studentServlet", value = "/students")
//public class StudentServlet extends HttpServlet {
//    record Student(String name, Integer grade) {}
//    List<Student> students = List.of(
//            new Student("jisoo", 1),
//            new Student("jennie", 2),
//            new Student("rose", 2),
//            new Student("lisa", 4)
//    );
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String responseJson = objectMapper.writeValueAsString(students);
//
//        response.setStatus(200);
//        response.setContentType("application/json");
//        response.getWriter().print(responseJson);
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String requestJson = parseRequestBody(request);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Student student = objectMapper.readValue(requestJson, Student.class);
//
//            // TODO: Save student to database
//
//            response.setStatus(200);
//            response.setContentType("application/json");
//            response.getWriter().print(objectMapper.writeValueAsString(student));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String parseRequestBody(HttpServletRequest request) throws IOException {
//        BufferedReader reader = request.getReader();
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            stringBuilder.append(line);
//        }
//        return stringBuilder.toString();
//    }
//}
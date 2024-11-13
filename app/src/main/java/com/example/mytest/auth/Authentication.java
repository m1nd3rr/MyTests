package com.example.mytest.auth;

import com.example.mytest.model.Student;
import com.example.mytest.model.Teacher;

public class Authentication {
    static public Student student;
    static public Teacher teacher;

    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {
        Authentication.student = student;
    }

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        Authentication.teacher = teacher;
    }

}

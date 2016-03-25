/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.service;

import by.Kursovaa.bean.Student;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vladimir
 */
@Service

public class StudentService {

    List<Student> list = new ArrayList<>();
    {
        list.add(new Student(1, "Ram"));
        list.add(new Student(2, "Shyam"));
        list.add(new Student(3, "Rahim"));
    }

    public List<Student> getStudents() {
        return list;
    }
}

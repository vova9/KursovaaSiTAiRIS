/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.bean;

/**
 *
 * @author Vladimir
 */
public class Student {

    private int stdId;
    private String stdName;

    public Student() {
    }

    public Student(int stdId, String stdName) {
        this.stdId = stdId;
        this.stdName = stdName;
    }

    public int getStdId() {
        return stdId;
    }

    public void setStdId(int stdId) {
        this.stdId = stdId;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.service;

/**
 *
 * @author Vladimir
 */
public class Path {

    private final boolean test = true;
    private final String WORK_DIR_TEST = "D:/mailArhiv/";

    public static Path getInterface() {
        return new Path();
    }

    public String getPath() {
        if (test) {
            return WORK_DIR_TEST;
        }
        String packag = this.getClass().getPackage().getName();
        String pathTemp = this.getClass().getResource("").getPath();
        return pathTemp.substring(0, (pathTemp.length() - (packag.length() + 1)));
    }
}

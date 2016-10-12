/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.service;

import by.kursovaa.dbAvtushkoVM.Kategorii;
import java.util.Comparator;

/**
 *
 * @author Vladimir
 */
public class SortKategorii implements Comparator<by.kursovaa.dbAvtushkoVM.Kategorii> {

    @Override
    public int compare(Kategorii t, Kategorii t1) {
        int id1 = t.getId();
        int id2 = t.getId();

        if (id1 > id2) {
            return 1;
        } else if (id1 < id2) {
            return -1;
        } else {
            return 0;
        }
    }
}

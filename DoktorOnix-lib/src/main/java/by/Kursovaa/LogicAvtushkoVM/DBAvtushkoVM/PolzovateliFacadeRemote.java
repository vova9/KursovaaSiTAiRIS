/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.Polzovateli;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface PolzovateliFacadeRemote {

    void create(Polzovateli polzovateli);

    void edit(Polzovateli polzovateli);

    void remove(Polzovateli polzovateli);

    Polzovateli find(Object id);

    List<Polzovateli> findAll();

    List<Polzovateli> findRange(int[] range);

    int count();
    
}

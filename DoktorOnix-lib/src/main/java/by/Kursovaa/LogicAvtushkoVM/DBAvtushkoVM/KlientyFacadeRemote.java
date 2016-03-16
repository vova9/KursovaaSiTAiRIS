/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.Klienty;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface KlientyFacadeRemote {

    void create(Klienty klienty);

    void edit(Klienty klienty);

    void remove(Klienty klienty);

    Klienty find(Object id);

    List<Klienty> findAll();

    List<Klienty> findRange(int[] range);

    int count();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Kategorii;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface KategoriiFacadeRemote {

    void create(Kategorii kategorii);

    void edit(Kategorii kategorii);

    void remove(Kategorii kategorii);

    Kategorii find(Object id);

    List<Kategorii> findAll();

    List<Kategorii> findRange(int[] range);

    int count();
    
}

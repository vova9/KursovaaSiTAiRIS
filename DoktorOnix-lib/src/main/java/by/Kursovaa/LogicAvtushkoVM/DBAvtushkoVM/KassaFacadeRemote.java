/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Kassa;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface KassaFacadeRemote {

    void create(Kassa kassa);

    void edit(Kassa kassa);

    void remove(Kassa kassa);

    Kassa find(Object id);

    List<Kassa> findAll();

    List<Kassa> findRange(int[] range);

    int count();
    
}

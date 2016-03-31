/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Zakazy;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface ZakazyFacadeRemote {

    void create(Zakazy zakazy);

    void edit(Zakazy zakazy);

    void remove(Zakazy zakazy);

    Zakazy find(Object id);

    List<Zakazy> findAll();

    List<Zakazy> findRange(int[] range);

    int count();
    
}

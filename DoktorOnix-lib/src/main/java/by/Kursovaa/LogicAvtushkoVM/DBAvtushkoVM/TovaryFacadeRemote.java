/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Tovary;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface TovaryFacadeRemote {

    void create(Tovary tovary);

    void edit(Tovary tovary);

    void remove(Tovary tovary);

    Tovary find(Object id);

    List<Tovary> findAll();

    List<Tovary> findRange(int[] range);

    int count();
    
}

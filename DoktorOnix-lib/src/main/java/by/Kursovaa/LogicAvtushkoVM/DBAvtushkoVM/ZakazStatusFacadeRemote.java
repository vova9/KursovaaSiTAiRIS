/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM.dbAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.ZakazStatus;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface ZakazStatusFacadeRemote {

    void create(ZakazStatus zakazStatus);

    void edit(ZakazStatus zakazStatus);

    void remove(ZakazStatus zakazStatus);

    ZakazStatus find(Object id);

    List<ZakazStatus> findAll();

    List<ZakazStatus> findRange(int[] range);

    int count();
    
}

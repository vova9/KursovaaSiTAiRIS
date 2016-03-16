/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.LogicAvtushkoVM.DBAvtushkoVM;

import by.Kursovaa.DBAvtushkoVM.ZakazInfo;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface ZakazInfoFacadeRemote {

    void create(ZakazInfo zakazInfo);

    void edit(ZakazInfo zakazInfo);

    void remove(ZakazInfo zakazInfo);

    ZakazInfo find(Object id);

    List<ZakazInfo> findAll();

    List<ZakazInfo> findRange(int[] range);

    int count();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.interfaces;

import javax.ejb.Remote;

/**
 *
 * @author Vladimir
 */
@Remote
public interface SeviceRemote {

    void importTovary();

    void importTovary(String url);
}

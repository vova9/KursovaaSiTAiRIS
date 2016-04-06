/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import java.io.Serializable;

/**
 *
 * @author Vladimir
 */
public class CoutMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private int newMess;
    private int cout;
    private int dmail;
    private int bmail;
    private int spammail;

    public CoutMessage() {
    }

    public int getNewMess() {
        return newMess;
    }

    public void setNewMess(int newMess) {
        this.newMess = newMess;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public void setDmail(int dmail) {
        this.dmail = dmail;
    }

    public void setBmail(int bmail) {
        this.bmail = bmail;
    }

    public void setSpammail(int spammail) {
        this.spammail = spammail;
    }

    public int getCout() {
        return cout;
    }

    public int getDmail() {
        return dmail;
    }

    public int getBmail() {
        return bmail;
    }

    public int getSpammail() {
        return spammail;
    }
}

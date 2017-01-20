/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.pojo;

import java.io.Serializable;

/**
 *
 * @author Vladimir
 */
public class CountMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private int newMess;
    private int cout;
    private int dmail;
    private int bmail;
    private int spammail;

    public CountMessage() {
        this.newMess = 0;
        this.cout = 0;
        this.dmail = 0;
        this.bmail = 0;
        this.spammail = 0;
    }

    public CountMessage(int newMess, int cout, int dmail, int bmail, int spammail) {
        this.newMess = newMess;
        this.cout = cout;
        this.dmail = dmail;
        this.bmail = bmail;
        this.spammail = spammail;
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

    @Override
    public String toString() {
        return "CoutMessage{" + "newMess=" + newMess + ", cout=" + cout + ", dmail=" + dmail + ", bmail=" + bmail + ", spammail=" + spammail + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.newMess;
        hash = 83 * hash + this.cout;
        hash = 83 * hash + this.dmail;
        hash = 83 * hash + this.bmail;
        hash = 83 * hash + this.spammail;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CountMessage other = (CountMessage) obj;
        if (this.newMess != other.newMess) {
            return false;
        }
        if (this.cout != other.cout) {
            return false;
        }
        if (this.dmail != other.dmail) {
            return false;
        }
        if (this.bmail != other.bmail) {
            return false;
        }
        return (this.spammail == other.spammail);
    }
}

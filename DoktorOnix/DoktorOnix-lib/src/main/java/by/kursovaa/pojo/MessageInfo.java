/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Vladimir
 */
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private long msgId;
    private String from;
    private String messageID;
    private ArrayList<String> to;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String subject;
    private String dateSent;
    private ArrayList<String> content;
    private boolean read;
    private boolean attachment;
    private boolean mark;
//    private MessagePriority priority;
    private ArrayList<FileMeta> attachments;
    private String directory;
    private int size;
    private boolean synchronize;
    private boolean select;
    private String toString;
    private String ccString;
    private String bccString;

    public MessageInfo() {
        this.attachments = new ArrayList<FileMeta>();
        this.content = new ArrayList<String>();
        this.to = new ArrayList<String>();
        this.cc = new ArrayList<String>();
        this.bcc = new ArrayList<String>();
        this.read = false;
        this.mark = false;
        this.attachment = false;
        this.select = false;
    }

    public MessageInfo(String directory) {
        this.directory = directory;

        this.attachments = new ArrayList<FileMeta>();
        this.content = new ArrayList<String>();
        this.to = new ArrayList<String>();
        this.cc = new ArrayList<String>();
        this.bcc = new ArrayList<String>();
        this.read = false;
        this.mark = false;
        this.attachment = false;
        this.select = false;
    }

    public MessageInfo(int id, long msgId, String from, String messageID, ArrayList<String> to,
            ArrayList<String> cc, ArrayList<String> bcc, String subject,
            String dateSent, ArrayList<String> content, boolean read,
            boolean attachment, boolean mark, ArrayList<FileMeta> attachments,
            String directory, int size, boolean synchronize, boolean select, String toString,
            String ccString, String bccString) {

        this.id = id;
        this.msgId = msgId;
        this.from = from;
        this.messageID = messageID;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.dateSent = dateSent;
        this.content = content;
        this.read = read;
        this.attachment = attachment;
        this.mark = mark;
        this.attachments = attachments;
        this.directory = directory;
        this.size = size;
        this.synchronize = synchronize;
        this.select = select;
        this.toString = toString;
        this.ccString = ccString;
        this.bccString = bccString;
    }

    public long getMsgId() {
        return msgId;
    }

    public String getFrom() {
        return from;
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getDateSent() {
        return dateSent;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isMark() {
        return mark;
    }

    public ArrayList<FileMeta> getAttachments() {
        return attachments;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void addRecipientTo(String to) {
        this.to.add(to);
    }

    public void addRecipientCc(String cc) {
        this.cc.add(cc);
    }

    public void addRecipientBcc(String bcc) {
        this.bcc.add(bcc);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public void addContent(String content) {
        this.content.add(content);
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void addAttachments(FileMeta attachmentName) {
        this.attachments.add(attachmentName);
    }

    public void setDirectory(String name) {
        directory = name;
    }

    public String getDirectory() {
        return directory;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSynchronize() {
        return synchronize;
    }

    public void setSynchronize(boolean synchronize) {
        this.synchronize = synchronize;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public String getCcString() {
        return ccString;
    }

    public void setCcString(String ccString) {
        this.ccString = ccString;
    }

    public String getBccString() {
        return bccString;
    }

    public void setBccString(String bccString) {
        this.bccString = bccString;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageBean{" + "msgId=" + msgId + ", from=" + from + ", to=" + to + ", cc=" + cc + ", bcc="
                + bcc + ", subject=" + subject + ", dateSent=" + dateSent + ", content=" + content + ", isNew="
                + read + ", mark=" + mark + ", priority=" + /*priority + */ ", attachments=" + attachments + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.msgId ^ (this.msgId >>> 32));
        hash = 53 * hash + Objects.hashCode(this.from);
        hash = 53 * hash + Objects.hashCode(this.subject);
        hash = 53 * hash + Objects.hashCode(this.dateSent);
        hash = 53 * hash + Objects.hashCode(this.content);
        hash = 53 * hash + (this.read ? 1 : 0);
        hash = 53 * hash + (this.mark ? 1 : 0);
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
        final MessageInfo other = (MessageInfo) obj;
        if (this.msgId != other.msgId) {
            return false;
        }
        if (this.read != other.read) {
            return false;
        }
        if (this.mark != other.mark) {
            return false;
        }
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.dateSent, other.dateSent)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }
}

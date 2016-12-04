/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Vladimir
 */
@JsonIgnoreProperties({"bytes"})
public class FileMeta implements Serializable {

    private static final long serialVersionUID = 4L;

    private String fileName;
    private String fileSize;
    private String fileType;
    private byte[] bytes;

    public FileMeta() {
    }

    public FileMeta(String fileName, String fileSize, String fileType, byte[] bytes) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.bytes = bytes;
    }

    public FileMeta(FileMeta other) {
        this(other.getFileName(), other.getFileSize(), other.getFileSize(), other.getBytes());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        System.out.println(Arrays.toString(bytes));
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "FileMeta{" + "fileName=" + fileName + ", fileSize=" + fileSize + ", fileType=" + fileType + ", bytes=" + bytes + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.fileName);
        hash = 89 * hash + Objects.hashCode(this.fileSize);
        hash = 89 * hash + Objects.hashCode(this.fileType);
        hash = 89 * hash + Arrays.hashCode(this.bytes);
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
        final FileMeta other = (FileMeta) obj;
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.fileSize, other.fileSize)) {
            return false;
        }
        if (!Objects.equals(this.fileType, other.fileType)) {
            return false;
        }
        if (!Arrays.equals(this.bytes, other.bytes)) {
            return false;
        }
        return true;
    }
}

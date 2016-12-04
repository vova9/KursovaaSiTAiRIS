/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.email.service;

import by.kursovaa.pojo.MessageInfo;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.sqlite.SQLiteException;

/**
 *
 * @author Vladimir
 */
public class DataBaseEmail {

    private static Connection conn = null;
    private static int count = 0;

    public static void connect(String path) throws ClassNotFoundException, SQLException, SQLiteException {
        System.out.println("DataBaseEmail::connect ST ");

        String url = "jdbc:sqlite:" + path + "/Email.db";
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(url);
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }

        System.out.println("DataBaseEmail::connect OK ");
    }

    public static void createTable(String folderName) throws ClassNotFoundException, SQLException, SQLiteException {
        System.out.println("DataBaseEmail::createTable ST ");

        Statement statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists [" + folderName + "] (messageId TEXT (255) PRIMARY KEY UNIQUE,"
                + "    date         TEXT,"
                + "    read         BOOLEAN    NOT NULL,"
                + "    mark         BOOLEAN    NOT NULL,"
                + "    synchronized BOOLEAN    NOT NULL,"
                + "    subject      VARCHAR,"
                + "    attachment   BOOLEAN    NOT NULL,"
                + "    size         INT,"
                + "    id           INT,"
                + "    recipientTo  TEXT,"
                + "    recipientCc  TEXT,"
                + "    recipientBcc TEXT,"
                + "    sender         TEXT);");

        statmt.close();
        System.out.println("DataBaseEmail::createTable OK ");
    }

    public static void insert(String folderName, MessageInfo info) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::insert ST ");

        String sql = "INSERT INTO [" + folderName + "] (messageId, date, read, mark, synchronized, subject, attachment, "
                + "size, id, recipientTo, recipientCc, recipientBcc, sender) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, info.getMessageID());//messageId
        pstmt.setString(2, info.getDateSent());//date
        pstmt.setBoolean(3, info.isRead());//read
        pstmt.setBoolean(4, info.isMark());//mark
        pstmt.setBoolean(5, info.isSynchronize());//synchronized
        pstmt.setString(6, info.getSubject());//subject
        pstmt.setBoolean(7, info.isAttachment());//attachment
        pstmt.setInt(8, info.getSize());//size
        pstmt.setInt(9, (int) info.getMsgId());//id

        String recipient = "";
        for (int i = 0; i < info.getTo().size(); i++) {
            recipient += info.getTo().get(i);
            recipient += ";";
        }
        pstmt.setString(10, recipient);//recipientTo

        recipient = "";
        for (int i = 0; i < info.getCc().size(); i++) {
            recipient += info.getCc().get(i);
            recipient += ";";
        }
        pstmt.setString(11, recipient);//recipientCc

        recipient = "";
        for (int i = 0; i < info.getBcc().size(); i++) {
            recipient += info.getBcc().get(i);
            recipient += ";";
        }
        pstmt.setString(12, recipient);//recipientBcc

        pstmt.setString(13, info.getFrom());//from
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("DataBaseEmail::insert OK ");
    }

    public static ArrayList<MessageInfo> getMessages(String folderName, int limit, int offset)
            throws SQLException, SQLiteException {

        System.out.println("DataBaseEmail::getMessages ST ");

        ArrayList<MessageInfo> listMail = new ArrayList<MessageInfo>();
        MessageInfo info = null;
        count = 0;

        Statement statmt = conn.createStatement();
        ResultSet resSet = statmt.executeQuery("SELECT * FROM [" + folderName + "] ORDER BY id DESC LIMIT "
                + limit + " OFFSET " + offset);

        while (resSet.next()) {
            info = new MessageInfo(folderName);
            initMessageInfo(info, resSet);
            listMail.add(info);
        }
        resSet.close();
        statmt.close();

        System.out.println("DataBaseEmail::getMessages OK");
        return listMail;
    }

    public static boolean getMessage(String folderName, String messageId, MessageInfo info)
            throws ClassNotFoundException, SQLException, SQLiteException {

        System.out.println("DataBaseEmail::getMessages ST ");

        Statement statmt = conn.createStatement();
        ResultSet resSet = statmt.executeQuery("SELECT * FROM [" + folderName + "] WHERE messageId = '" + messageId + "'");

        if (resSet.next()) {
            initMessageInfo(info, resSet);

            resSet.close();
            statmt.close();

            System.out.println("DataBaseEmail::getMessages OK true");
            return true;
        }
        resSet.close();
        statmt.close();

        System.out.println("DataBaseEmail::getMessages OK false");
        return false;
    }

    public static void updateSynchronized(String folderName) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::updateSynchronized STOK ");
        Statement statmt = conn.createStatement();
        statmt.execute("UPDATE [" + folderName + "] SET synchronized=0 WHERE synchronized = 1;");
        statmt.close();
    }

    public static void updatedSynchronized(String folderName, String messageId) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::updatedSynchronized STOK");
        Statement statmt = conn.createStatement();
        statmt.execute("UPDATE [" + folderName + "] SET synchronized=1 WHERE messageId = '" + messageId + "';");
        statmt.close();
    }

// markUn
    public static void updatedUnSynchronizedMark(String folderName, boolean mark, String messageId)
            throws SQLException, SQLiteException {

        System.out.println("DataBaseEmail::updatedUnSynchronizedMark STOK " + messageId);

        String sql = "UPDATE [" + folderName + "] SET synchronized=0, mark=? WHERE messageId =?;";
        //markUn =1
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, mark);//mark
        pstmt.setString(2, messageId);//messageId   
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static void updateSynchronizedAll() throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::updateSynchronizedAll ST");

        DatabaseMetaData md = conn.getMetaData();
        ResultSet resSet = md.getTables(null, null, "%", null);
        Statement statmt = conn.createStatement();
        while (resSet.next()) {
            statmt.execute("UPDATE [" + resSet.getString(3) + "] SET synchronized = 0 WHERE synchronized = 1;");
        }
        resSet.close();
        statmt.close();

        System.out.println("DataBaseEmail::updateSynchronizedAll OK");
    }

    public static void updateAll(String folderName, MessageInfo info) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::update ST");

        String sql = "UPDATE [" + folderName + "] SET date=?, read=?, mark=?, synchronized=?, subject=?, attachment=?,"
                + "size=?, id=?, recipientTo=?, recipientCc=?, recipientBcc=?, sender=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, info.getDateSent());//date
        pstmt.setBoolean(2, info.isRead());//read
        pstmt.setBoolean(3, info.isMark());//mark
        pstmt.setBoolean(4, info.isSynchronize());//synchronized
        pstmt.setString(5, info.getSubject());//subject
        pstmt.setBoolean(6, info.isAttachment());//attachment
        pstmt.setInt(7, info.getSize());//size
        pstmt.setInt(8, (int) info.getMsgId());//id

        String recipient = "";
        for (int i = 0; i < info.getTo().size(); i++) {
            recipient += info.getTo().get(i);
            recipient += ";";
        }
        pstmt.setString(9, recipient);//recipientTo

        recipient = "";
        for (int i = 0; i < info.getCc().size(); i++) {
            recipient += info.getCc().get(i);
            recipient += ";";
        }
        pstmt.setString(10, recipient);//recipientCc

        recipient = "";
        for (int i = 0; i < info.getBcc().size(); i++) {
            recipient += info.getBcc().get(i);
            recipient += ";";
        }
        pstmt.setString(11, recipient);//recipientBcc

        pstmt.setString(12, info.getFrom());//from
        pstmt.setString(13, info.getMessageID());//messageId
        pstmt.executeUpdate();
        pstmt.close();

        System.out.println("DataBaseEmail::update OK ");
    }

    public static void updatedMessageNumber(String folderName, String messageID, int messageNumber)
            throws SQLException, SQLiteException {

        System.out.println("DataBaseEmail::updatedMessageNumber ST");

        String sql = "UPDATE [" + folderName + "] SET id=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, messageNumber);//id
        pstmt.setString(2, messageID);//messageId
        pstmt.executeUpdate();

        pstmt.close();
        System.out.println("DataBaseEmail::updatedMessageNumber OK ");
    }

    public static void updatedFlags(String folderName, MessageInfo info) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::updatedFlags ST");

        String sql = "UPDATE [" + folderName + "] SET read=?, mark=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, info.isRead());//read
        pstmt.setBoolean(2, info.isMark());//mark
        pstmt.setString(3, info.getMessageID());//messageId
        pstmt.executeUpdate();

        pstmt.close();
        System.out.println("DataBaseEmail::updatedFlags OK ");
    }

    public static void updatedAttachment(String folderName, boolean attachment, String messageID)
            throws SQLException, SQLiteException {

        System.out.println("DataBaseEmail::updatedAttachment ST");

        String sql = "UPDATE [" + folderName + "] SET attachment=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, attachment);//read
        pstmt.setString(2, messageID);//messageId
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("DataBaseEmail::updatedAttachment OK");
    }

    public static void close() throws ClassNotFoundException, SQLException, SQLiteException {
        System.out.println("DataBaseEmail::close ST ");

        if (conn != null && !conn.isClosed()) {
            conn.close();
        }

        System.out.println("DataBaseEmail::close OK ");
    }

    public static int countRecord(String folderName) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::countRecord ST");

        Statement statmt = conn.createStatement();
        ResultSet resSet
                = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name= '" + folderName + "';");

        if (resSet.next()) {
            ResultSet res
                    = statmt.executeQuery("SELECT COUNT(*) FROM [" + folderName + "];");

            int count = res.getInt(1);
            res.close();
            resSet.close();
            statmt.close();

            System.out.println("DataBaseEmail::countRecord OK true");
            return count;
        }
        resSet.close();
        statmt.close();

        System.out.println("DataBaseEmail::countRecord OK false");
        return 0;
    }

    public static int countUnRead(String folderName) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::countRecord ST");

        Statement statmt = conn.createStatement();
        ResultSet resSet
                = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name= '" + folderName + "';");

        if (resSet.next()) {
            ResultSet res
                    = statmt.executeQuery("SELECT COUNT(*) FROM [" + folderName + "] WHERE read = 0;");

            int count = res.getInt(1);
            res.close();
            resSet.close();
            statmt.close();

            System.out.println("DataBaseEmail::countRecord OK true");
            return count;
        }
        resSet.close();
        statmt.close();

        System.out.println("DataBaseEmail::countRecord OK false");
        return 0;
    }

    public static void updatedMarkMessage(String folderName, String messageID, boolean b, boolean a)
            throws SQLException, SQLiteException {

        String sql = "UPDATE [" + folderName + "] SET mark=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, b);//mark
        pstmt.setString(2, messageID);//messageId
        pstmt.executeUpdate();
        pstmt.cancel();
    }

    public static void updatedReadMessage(String folderName, String messageID, boolean b, boolean b0)
            throws SQLException, SQLiteException {

        String sql = "UPDATE [" + folderName + "] SET read=? WHERE messageId =?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, b);//read
        pstmt.setString(2, messageID);//messageId
        pstmt.executeUpdate();
        pstmt.cancel();
    }

//TODO
    public static void updatedUnSynchronizedRead(String directory, String messageID)
            throws SQLException, SQLiteException {
    }

//TODO
    public static void deleteMessage(String directory, String messageID) throws SQLException, SQLiteException {
    }

//TODO
    public static void deleteMessageNeed(String directory, String messageID) throws SQLException, SQLiteException {
    }

//TODO
    public static void deleteMessageNeed(ArrayList<MessageInfo> listMail) throws SQLException, SQLiteException {
    }

//TODO
    public static void updatedUnSynchronizedRead(ArrayList<MessageInfo> listMail) throws SQLException, SQLiteException {
    }

    private static void initMessageInfo(MessageInfo info, ResultSet resSet) throws SQLException, SQLiteException {
        System.out.println("DataBaseEmail::initMessageInfo ST ");

        info.setMessageID(resSet.getString("messageId"));// messageId
        info.setDateSent(resSet.getString("date"));//date
        info.setRead(resSet.getBoolean("read"));//read
        info.setMark(resSet.getBoolean("mark"));//mark
        info.setSynchronize(resSet.getBoolean("synchronized"));//synchronized
        info.setSubject(resSet.getString("subject"));//subject
        info.setAttachment(resSet.getBoolean("attachment"));//attachment
        info.setSize(resSet.getInt("size"));//size
        info.setMsgId(resSet.getInt("id"));//id

        String recipient = resSet.getString("recipientTo");//recipientTo
        String[] recipients = recipient.split(";");
        for (int i = 0; i < recipients.length; i++) {
            info.addRecipientTo(recipients[i]);
        }

        recipient = resSet.getString("recipientCc");//recipientTo
        recipients = recipient.split(";");
        for (int i = 0; i < recipients.length; i++) {
            info.addRecipientCc(recipients[i]);
        }

        recipient = resSet.getString("recipientBcc");//recipientTo
        recipients = recipient.split(";");
        for (int i = 0; i < recipients.length; i++) {
            info.addRecipientBcc(recipients[i]);
        }

        count++;
        info.setFrom(resSet.getString("sender"));//from
        info.setId(count);

        System.out.println("DataBaseEmail::initMessageInfo OK ");
    }

}

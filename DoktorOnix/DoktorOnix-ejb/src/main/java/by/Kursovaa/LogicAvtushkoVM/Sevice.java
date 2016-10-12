/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.logicAvtushkoVM;

import by.kursovaa.dbAvtushkoVM.Kategorii;
import by.kursovaa.dbAvtushkoVM.Tovary;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.KategoriiFacade;
import by.kursovaa.logicAvtushkoVM.dbAvtushkoVM.TovaryFacade;
import java.io.BufferedInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Vladimir
 */
@Stateful
public class Sevice implements SeviceRemote {

    @EJB
    KategoriiFacade kategoriiFacade;
    @EJB
    TovaryFacade tovaryFacade;
         
    private int readBytes = 0;

    @Override
    public void importTovary(String urlAdres) {
        try {
            URL url = new URL(urlAdres);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            File f1 = new File("C://import.xml");
            FileOutputStream fw = new FileOutputStream(f1);
            byte[] btBuffer = new byte[1024];
            int intRead = 0;
            while ((intRead = bis.read(btBuffer)) != -1) {
                fw.write(btBuffer, 0, intRead);
                readBytes = readBytes + intRead;
            }
            fw.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Sevice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void importTovary() {
        try {
            File fXmlFile = new File("C://import.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("category");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("parentId").isEmpty()) {
                        Kategorii kategoria = new Kategorii(Integer.parseInt(eElement.getAttribute("id")),
                                eElement.getTextContent(), 0);
                        System.out.println(kategoria.getName()+' '+kategoria.getId());
                        kategoriiFacade.edit(kategoria);
                    } else {
                        Kategorii kategoria = new Kategorii(Integer.parseInt(eElement.getAttribute("id")),
                                eElement.getTextContent(), Integer.parseInt(eElement.getAttribute("parentId")));
                        System.out.println(kategoria.getName()+' '+kategoria.getId()+' '+kategoria.getRoditel());
                        kategoriiFacade.edit(kategoria);
                    }
                }
            }
            nList = doc.getElementsByTagName("offer");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Tovary tovar = new Tovary(
                            Integer.parseInt(eElement.getElementsByTagName("vendorCode").item(0).getTextContent()),
                            eElement.getElementsByTagName("name").item(0).getTextContent(),
                            Float.parseFloat(eElement.getElementsByTagName("price").item(0).getTextContent()));
                    tovar.setOpisanie(eElement.getElementsByTagName("description").item(0).getTextContent());
                    tovar.setKategoria(kategoriiFacade.find(Integer.parseInt(eElement.getElementsByTagName("categoryId").item(0).getTextContent())));
                    tovaryFacade.edit(tovar);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Sevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

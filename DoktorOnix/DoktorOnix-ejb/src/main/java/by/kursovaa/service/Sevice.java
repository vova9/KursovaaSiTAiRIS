/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.service;

import by.kursovaa.interfaces.SeviceRemote;
import by.kursovaa.entity.Kategorii;
import by.kursovaa.entity.Tovary;
import by.kursovaa.jpa.KategoriiFacade;
import by.kursovaa.jpa.TovaryFacade;
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
import javax.ejb.Stateful;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Vladimir
 */
@Stateful
public class Sevice implements SeviceRemote {

    private KategoriiFacade kategoriiFacade;
    private TovaryFacade tovaryFacade;
    private int readBytes = 0;
    private final String PATH = Path.getInterface().getPath() + File.separator + "import.xml";

    @Override
    public void importTovary(String urlAdres) {
        try {
            URL url = new URL(urlAdres);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            File f1 = new File(PATH);
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
        kategoriiFacade = new KategoriiFacade();
        tovaryFacade = new TovaryFacade();

        try {
            File fXmlFile = new File(PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("category");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Kategorii kategoria = new Kategorii();

                    if (eElement.getAttribute("parentId").isEmpty()) {
                        kategoria.setId(Integer.parseInt(eElement.getAttribute("id")));
                        kategoria.setName(eElement.getTextContent());
                        kategoria.setRoditel(0);

                        System.out.println(kategoria.getName() + ' ' + kategoria.getId());

                    } else {
                        kategoria.setId(Integer.parseInt(eElement.getAttribute("id")));
                        kategoria.setName(eElement.getTextContent());
                        kategoria.setRoditel(Integer.parseInt(eElement.getAttribute("parentId")));

                        System.out.println(kategoria.getName() + ' ' + kategoria.getId() + ' '
                                + kategoria.getRoditel());
                        kategoriiFacade.edit(kategoria);
                    }
                    kategoriiFacade.edit(kategoria);
                }
            }
            nList = doc.getElementsByTagName("offer");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Tovary tovar = new Tovary();
                    tovar.setArtikul(Integer.parseInt(eElement.getElementsByTagName("vendorCode").item(0).getTextContent()));
                    tovar.setNaimenovanie(eElement.getElementsByTagName("name").item(0).getTextContent());
                    tovar.setZena(Float.parseFloat(eElement.getElementsByTagName("price").item(0).getTextContent()));
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

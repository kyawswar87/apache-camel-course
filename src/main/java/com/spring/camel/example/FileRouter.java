package com.spring.camel.example;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class FileRouter {
    private static final Logger logger = Logger.getLogger(FileRouter.class.getName());

    public static void main(String[] args) {
        File srcDir = new File("src/data");
        File ukDir = new File("target/messages/uk");
        File othersDir = new File("target/messages/others");

        // Ensure target directories exist
        ukDir.mkdirs();
        othersDir.mkdirs();

        // Process each file in the source directory
        for (File file : srcDir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                processFile(file, ukDir, othersDir);
            }
        }
    }

    private static void processFile(File file, File ukDir, File othersDir) {
        try {
            // Parse XML file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            // Evaluate XPath expression
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expr = xPath.compile("/person/city = 'London'");
            Boolean isUkMessage = (Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);

            // Log and move file based on the condition
            if (isUkMessage) {
                logger.info("UK message");
                moveFile(file.toPath(), ukDir.toPath());
            } else {
                logger.info("Other message");
                moveFile(file.toPath(), othersDir.toPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void moveFile(Path source, Path targetDir) {
        try {
            Files.move(source, targetDir.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

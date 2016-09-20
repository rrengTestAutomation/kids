package com.tvokids.rerun;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class for parse the config file.
 * @best 
 *
 */
public class xmlParser {
	
		public static Map<String, String> parseConfigFileXMLforVariables(
				String xmlPath, String tagName) throws Exception {
			try {
				Map<String, String> configs = new HashMap<String, String>();

				Element root = readXML(xmlPath);
				for (Element variables : nodeListToElementList(root.getElementsByTagName(tagName))) {
					for (Element item : nodeListToElementList(variables.getElementsByTagName("item"))) {
						configs.put(item.getAttribute("name"),
								item.getAttribute("value"));
					}
				}
				return configs;
			} catch (Throwable t) {
				// Logger.writeErrorLogEntry(t.getMessage());
				throw new Exception(t.getMessage());
			}
		}

		private static Element readXML(String fileLocation) throws Exception {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(new File(fileLocation));
				dom.getDocumentElement().normalize();
				return dom.getDocumentElement();
			} catch (Throwable t) {
				// Logger.writeErrorLogEntry(t.getMessage());
				throw new Exception(t.getMessage());
			}
		}

		private static List<Element> nodeListToElementList(NodeList nl)
				throws Exception {
			try {
				List<Element> lElements = new ArrayList<Element>();
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0; i < nl.getLength(); i++) {
						if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
							lElements.add((Element) nl.item(i));
						}
					}
				}
				return lElements;
			} catch (Throwable t) {
				// Logger.writeErrorLogEntry(t.getMessage());
				throw new Exception(t.getMessage());
			}
		}

}

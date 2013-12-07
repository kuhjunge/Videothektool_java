import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Chris Deter
 * @version 1.1
 */
public class SaveLoader {

	private static String username = "";
	private static String password = "";
	private static String url = "";
	private static String dbName = "";
	/**
	 * SavePath für die Login-Daten Im Verzeichnis der Anwendung
	 */
	private static String savepath = /* defaultDirectory() + File.separator + */"dbcon.xml";
	private static boolean saveuser = false;

	/**
	 * @return the username
	 */
	public static String getSavepath() {
		return savepath;
	}
	
	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public static void setUsername(String un) {
		username = un;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public static void setPassword(String pw) {
		password = pw;
	}

	/**
	 * @return the url
	 */
	public static String getUrl() {
		return url;
	}
	
	/**
	 * @return the url
	 */
	public static String getcleanUrl() {
		return url.replace("jdbc:mysql://", "");
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public static void setUrl(String urli) {
		url = "jdbc:mysql://" + urli.replace("jdbc:mysql://", "");
	}

	/**
	 * @return the dbName
	 */
	public static String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName
	 *            the dbName to set
	 */
	public static void setDbName(String dbn) {
		dbName = dbn;
	}

	/*
	 * private static String defaultDirectory() { String OS =
	 * System.getProperty("os.name").toUpperCase(); if (OS.contains("WIN"))
	 * return System.getenv("APPDATA"); else if (OS.contains("MAC")) return
	 * System.getProperty("user.home") + "/Library/Application " + "Support";
	 * else if (OS.contains("NUX")) return System.getProperty("user.home");
	 * return System.getProperty("user.dir"); }
	 */

	/**
	 * Speichert eine XML Datei mit Filminformationen in den entsprechenden
	 * Ordner
	 */
	public static void saveToXML() {
		Document dom;
		Element e = null;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			// create the root element
			Element rootEle = dom.createElement("dbcon");

			// create data elements and place them under root
			e = dom.createElement("username");
			e.appendChild(dom.createTextNode(saveuser ? username : ""));
			rootEle.appendChild(e);

			e = dom.createElement("password");
			e.appendChild(dom.createTextNode(saveuser ? password : ""));
			rootEle.appendChild(e);

			e = dom.createElement("url");
			e.appendChild(dom.createTextNode(url.replace("jdbc:mysql://", "")));
			rootEle.appendChild(e);

			e = dom.createElement("dbName");
			e.appendChild(dom.createTextNode(dbName));
			rootEle.appendChild(e);

			dom.appendChild(rootEle);

			try {
				Transformer tr = TransformerFactory.newInstance()
						.newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				// tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "movie.dtd");
				tr.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(
						new FileOutputStream(savepath)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out
					.println("UsersXML: Error trying to instantiate DocumentBuilder "
							+ pce);
		}
	}

	public static void read() {
		File d = new File(savepath);
		if (d.exists()) {
			readXML();
		}
	}

	/**
	 * Liest die XML Datei aus dem Verzeichnis des Filmes
	 * 
	 * @param xml
	 *            - Der Pfad der Datei
	 * @return IMDB ID
	 */
	private static void readXML() {
		ArrayList<String> rolev = new ArrayList<String>();
		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the
			// XML file
			dom = db.parse(savepath);

			Element doc = dom.getDocumentElement();

			username = getTextValue(username, doc, "username");
			if (username != null) {
				if (!username.isEmpty())
					rolev.add(username);
			}

			password = getTextValue(password, doc, "password");
			if (password != null) {
				if (!password.isEmpty())
					rolev.add(password);
			}
			url = "jdbc:mysql://" + getTextValue(url, doc, "url");
			if (url != null) {
				if (!url.isEmpty())
					rolev.add(url);
			}
			dbName = getTextValue(dbName, doc, "dbName");
			if (dbName != null) {
				if (!dbName.isEmpty())
					rolev.add(dbName);
			}
			if (username != "" && password != "")
				saveuser = true; // Nutzerdaten werden standardmäßig gespeichert

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * } catch (ParserConfigurationException pce) {
		 * System.out.println(pce.getMessage()); } catch (SAXException se) {
		 * System.out.println(se.getMessage()); } catch (IOException ioe) {
		 * System.err.println(ioe.getMessage()); }
		 */
	}

	private static String getTextValue(String def, Element doc, String tag) {
		String value = def;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			value = nl.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}

	/**
	 * @return the saveuser
	 */
	public static boolean isSaveuser() {
		return saveuser;
	}

	/**
	 * @param saveuser
	 *            the saveuser to set
	 */
	public static void setSaveuser(boolean su) {
		saveuser = su;
	}

}

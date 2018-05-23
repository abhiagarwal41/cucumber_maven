package com.counterparty.automation.utilities;

import static com.counterparty.automation.utilities.APIUtility.postRequest;
import static com.counterparty.automation.utilities.Constants.dbUtility;
import static com.counterparty.automation.utilities.FileUtility.convertFileToString;
import static com.counterparty.automation.utilities.FileUtility.convertStringToXMLDoc;
import static com.counterparty.automation.utilities.FileUtility.getProperty;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DocumentSource;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.counterparty.automation.model.Counterparty;
import com.counterparty.automation.model.SshConnectionManager;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Functions {

	final static Logger logger = Logger.getLogger(Functions.class);

	public static void initializeDB() {
		String tcp = getProperty("DATABASE_TCP");
		String database = getProperty("DATABASE_APAC_WRIE");
		String db_username = getProperty("DATABASE_USERNAME");
		String db_password = getProperty("DATABASE_PASSWORD");
		dbUtility = new DBUtility(tcp, database, db_username, db_password);
		dbUtility.makeConnection();
	}

	public static void closDBconnection() {
		System.out.println("dbUtility" + dbUtility);
		if (dbUtility != null)
			dbUtility.closeConnection();

	}
	
	public static int getTotalRowCountFromPTXTable(Long tradableInfoId) {
		String sqlQuery = "select * from InstrumentPTXInfo where " + "tradableInfoID =" + tradableInfoId;
		Statement statement = dbUtility.createStatement();
		ResultSet rs = null;
		try {
			rs = dbUtility.executeQuery(statement, sqlQuery);
			rs.last();
			int rowcount = rs.getRow();
			return rowcount;
		} catch (Exception e) {
			System.out.println("Exception while fetching rows from db:" + e.getMessage());
			logger.error("Exception while fetching rows from db:" + e.getMessage());
			return -1;
		} finally {
			dbUtility.close(rs).close(statement);
		}
	}
	
	public static void createRetryFileOnUnix(LinkedList<Long> tics, String region){
		
		for(Long tic:tics)
			appendFile(ticsFilePath, tic.toString());
	
		
		String hostname = getProperty("UNIX_HOSTNAME_"+region);
		String username = getProperty("userName");
		String password = getProperty("password");
		String directory = getProperty("UNIX_DIRECTORY");
		String sudoCommand = "sudo su - rokdev";
		SshConnectionManager sshConnectionManager = new SshConnectionManager();
		sshConnectionManager.connect(hostname, username, password);
		sshConnectionManager.sudoUser(sudoCommand);
		sshConnectionManager.executeCommand(password, 15);
		System.out.println("Creating retry file on UNIX box:"+hostname);
		logger.info("Creating retry file on UNIX box:"+hostname);
		try {
			sshConnectionManager.upload(directory, ticsFilePath);
			System.out.println("Retry file created:" + directory + "/tics.txt");
			logger.info("Retry file created:" + directory + "/tics.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		sshConnectionManager.close();

		
	}


	public static void deleteExistingCounterpartyData()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {

		String cconsol = "";

		// read cconsols from json file
		/*
		 * Gson gson = new Gson(); Type type = new
		 * TypeToken<List<Counterparty>>() {}.getType(); List<Counterparty>
		 * counterparties = gson.fromJson(new
		 * FileReader(Constants.counterpartiesJsonFilePath), type);
		 * 
		 * for (Counterparty counterparty : counterparties) { cconsol = cconsol
		 * + "," + counterparty.getTradingAccountCConsol(); }
		 */

		// read cconsols from text file

		try {
			File file = new File(Constants.cconsolFilePath);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				cconsol = cconsol + "," + line;
			}
			fileReader.close();
			if(cconsol.length()>0){
			cconsol = cconsol.substring(1); // remove first comma
			clearFileContents(Constants.cconsolFilePath);
			deleteCounterpartyData(cconsol);
			}
		} catch (IOException e) {
			logger.error("unable to read cconsol from text file " + e.getMessage());
		}
		
	}

	public static void deleteAllFilesInFolder(String directory) throws IOException {
		File folder = new File(directory);
		if (folder.exists() && folder.isDirectory())
			FileUtils.cleanDirectory(folder);
	}

	public static void deleteCounterpartyData(String cconsol) {

		logger.info("Going to delete following cconsols from Counterparty and CptyTradingAccount tables : " + cconsol);
		String sqlQueryCptyTradingAccount = "DELETE FROM CptyTradingAccount where cconsol in (" + cconsol + ")";
		String sqlQueryCounterparty = "DELETE FROM Counterparty where cconsol in (" + cconsol + ")";
		Statement statement = dbUtility.createStatement();
		try {
			dbUtility.updateQuery(statement, sqlQueryCptyTradingAccount);
			dbUtility.updateQuery(statement, sqlQueryCounterparty);
		} catch (Exception e) {
			logger.error("Counterparties Delete unsuccessful " + e.getMessage());
		} finally {
			dbUtility.close(statement);
		}
	}

	public static void clearFileContents(String path) {
		PrintWriter writer = null;
		logger.info("clearing contents of file " + path);
		try {
			writer = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			logger.error("Unable to clear file contents " + e.getMessage());
		}
		writer.print("");
		writer.close();
	}

	public static void createCounterpartyData(Counterparty counterparty) {

		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		if (counterparty.getXmltype().equalsIgnoreCase("TRADINGACCOUNT"))
			urlParameters.add(new BasicNameValuePair("tradingAccountMessage",
					convertFileToString(counterparty.getLinguaMessageFile())));
		else
			urlParameters.add(new BasicNameValuePair("legalEntityMessage",
					convertFileToString(counterparty.getLinguaMessageFile())));
		urlParameters.add(new BasicNameValuePair("xrefHeader", counterparty.getXrefHeader()));
		urlParameters.add(new BasicNameValuePair("cConsolVersionHeader", counterparty.getcConsolVersionHeader()));
		urlParameters.add(new BasicNameValuePair("tradingAccountCConsol", counterparty.getTradingAccountCConsol()));
		String urlToPost = counterparty.getUrlToPost();
		logger.info("posting counterparty data for cconsol:" + counterparty.getTradingAccountCConsol());
		HttpResponse response = postRequest(urlToPost, urlParameters);
		if (response == null)
			fail("Unable to post counterparty data");
		logger.info(response);
		try {
			logger.info("Waiting for 20 seconds for counterparty insert into db");
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void restartCounterpartyWebService() {
		String hostname = getProperty("UNIX_HOSTNAME");
		String username = getProperty("UNIX_USERNAME");
		String password = getProperty("UNIX_PASSWORD");
		password = new String(parseBase64Binary(password));
		String sudoCommand = "sudo su - rokdev";
		/*
		 * String stopWebServiceCommand =
		 * "sh /sbcimp/dyn/data/GR/STP/ctx/maven_counterparty/apac/uat/sub/stop.sh"
		 * ; String startWebServiceCommand =
		 * "sh /sbcimp/dyn/data/GR/STP/ctx/maven_counterparty/apac/uat/sub/start.sh"
		 * ;
		 */
		String stopWebServiceCommand = "sh /sbcimp/dyn/data/GR/STP/ctx/maven_counterparty/apac/qa_apac_write/sub/stop.sh";
		String startWebServiceCommand = "sh /sbcimp/dyn/data/GR/STP/ctx/maven_counterparty/apac/qa_apac_write/sub/start.sh";
		SshConnectionManager sshConnectionManager = new SshConnectionManager();
		sshConnectionManager.connect(hostname, username, password);
		sshConnectionManager.sudoUser(sudoCommand);
		sshConnectionManager.executeCommand(password, 15);
		sshConnectionManager.executeCommand(stopWebServiceCommand, 10);
		sshConnectionManager.executeCommand(startWebServiceCommand, 240);
		sshConnectionManager.close();

	}

	/*
	 * public static String nodeToString(Node node) {
	 * 
	 * StringWriter sw = new StringWriter(); try { Transformer t =
	 * TransformerFactory.newInstance().newTransformer();
	 * t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	 * t.setOutputProperty(OutputKeys.INDENT, "yes"); t.transform(new
	 * DOMSource(node), new StreamResult(sw)); } catch (TransformerException te)
	 * { logger.error("nodeToString Transformer Exception " + te.getMessage());
	 * } return sw.toString(); }
	 */

	public static void updateLinguaTags(List<LinkedList<String>> rows, String linguaFile) {

		File tempfile = null;
		Map<String, String> returnMap = new HashMap<>();
		try {

			String fileAsString = convertFileToString(linguaFile);
			Document doc = convertStringToXMLDoc(fileAsString);

			deleteFile(Constants.updatedLinguaFilePath);

			for (List<String> row : rows) {

				String tagxmltagName = row.get(0);
				String propertyName = row.get(1);
				String updatedTagValue = row.get(2);

				if (propertyName.equalsIgnoreCase("N/A")) {

					if (updatedTagValue.equalsIgnoreCase("DELETE")) {
						Node node = doc.selectSingleNode(tagxmltagName);
						if (node == null)
							fail("Node not found: " + tagxmltagName);
						else {
							logger.info("Deleting tag :" + tagxmltagName);
							node.detach();
						}
					} else {
						logger.info("Updating value of tag :" + tagxmltagName);
						Node node = doc.selectSingleNode(tagxmltagName);
						if (node == null)
							fail("Node not found: " + tagxmltagName);
						else {
							logger.info("Setting value :" + updatedTagValue);
							node.setText(updatedTagValue);
						}
					}

				} else {

					Node node = doc.selectSingleNode(tagxmltagName);
					if (node == null)
						fail("Node not found: " + tagxmltagName);
					else {
						Element element = (Element) node;
						logger.info("Updating tag :" + tagxmltagName);
						logger.info("Updating value of property :" + propertyName);
						logger.info("Setting value :" + updatedTagValue);
						element.setAttributeValue(propertyName, updatedTagValue);
					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DocumentSource source = new DocumentSource(doc);

				logger.info("Creating new lingua file with updated tag values");
				tempfile = new File(Constants.updatedLinguaFilePath);
				tempfile.createNewFile();
				StreamResult result = new StreamResult(tempfile);
				transformer.transform(source, result);

			}

		} catch (TransformerException tfe) {
			logger.error(tfe.getMessage());
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}

		// returnMap.put("filename", tempfile.getName());
		// return returnMap;

	}

	public static void appendCconsolFile(String path, String cconsol) {
		try (FileWriter fw = new FileWriter(path, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(cconsol);
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public static void deleteFile(String path) {
		try {

			File file = new File(path);
			logger.info("Going to delete file :" + file.getName());
			if (file.exists()) {
				if (file.delete()) {
					logger.info(file.getName() + " is deleted!");
				} else {
					logger.error("Delete operation is failed.");
				}
			}

		} catch (Exception e) {

			logger.error(e.getMessage());

		}
	}

	public static long getRandomCConsol() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		long milliseconds = 0;
		try {
			Date d = dateFormat.parse(dateFormat.format(date));
			milliseconds = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Random cconsol generated: " + milliseconds / 1000);
		return milliseconds / 1000;
	}

	public static List<LinkedList<String>> returnModifiableList(List<List<String>> originalrows) {
		List<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();
		for (List<String> row : originalrows) {
			LinkedList<String> newrow = new LinkedList<String>();
			for (int i = 0; i < row.size(); i++) {
				newrow.add(row.get(i));
			}
			rows.add(newrow);
		}
		return rows;
	}

	public static Counterparty getCounterpartyObjectFromType(String counterpartyType) {
		Gson gson = new Gson();
		Type type = new TypeToken<List<Counterparty>>() {
		}.getType();

		List<Counterparty> counterparties = null;
		try {
			counterparties = gson.fromJson(new FileReader(Constants.counterpartiesJsonFilePath), type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Counterparty counterparty = null;
		for (Counterparty cpty : counterparties) {
			if (cpty.getXmltype().equalsIgnoreCase(counterpartyType)) {
				counterparty = cpty;
				break;
			}
		}
		return counterparty;
	}

	public static Document convertStringToXMLDoc(String xmlString) {

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new InputSource(new StringReader(xmlString)));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error("unable to covert string to xml: " + e.getMessage());
		}
		return document;
	}
	
	public static String convertFileToString(String fXmlFile) {
		
		String fileAsString = "";
		try {
			inputStream = new FileInputStream(fXmlFile);
			fileAsString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			logger.error("unable to get file message " + e.getMessage());
		}
		return fileAsString;
	}

}

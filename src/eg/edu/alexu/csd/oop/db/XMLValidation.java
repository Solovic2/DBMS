package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLValidation {

	private static XMLValidation  instance = new XMLValidation();
	/***************************Singleton design pattern*****************************/
	private XMLValidation() {
		
	}
	
	public static XMLValidation get_instance() {
		return instance;
	}
	
	/****************************************** ***************************************/
	
	public boolean validate_Xml_Xsd(String XML_path , String XSD_path ) {
		
		SchemaFactory schemafactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = schemafactory.newSchema(new File(XSD_path));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validator validator = schema.newValidator();
		File file = new File(XML_path);
		StreamSource streamsource  = new StreamSource(file); 
			try {
				validator.validate(streamsource);
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		return true;
		
	}
	
	
}
package serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import serialization.dateSerializer.XmlDateSerialization;

public class XmlSerialize<T> implements Serialization<T> {
	
	private Class<T> clas;

	public Class<T> getClas() {
    	return clas;
    }

    public void setClas(Class<T> clas) {
    	this.clas = clas;
    }

    public XmlSerialize(Class<T> clas) {
    	this.clas = clas;
    }
    
    @Override
	public void toFile(ArrayList<T> object, File file) {
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		xStream.registerConverter(new XmlDateSerialization());
		String strObj = xStream.toXML(object);
        FileWriter writer = null;
        try {
        	writer = new FileWriter(file);
            writer.write(strObj);
            writer.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	@SuppressWarnings("unchecked")
    @Override
    public ArrayList<T> fromFile(File file) throws Exception {
		XStream xStream = new XStream(new DomDriver());
        FileReader fileReader = null;
        try {
        	fileReader = new FileReader(file);
        	return (ArrayList<T>) xStream.fromXML(fileReader);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        return null;
	}
}

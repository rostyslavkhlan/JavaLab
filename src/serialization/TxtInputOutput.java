package serialization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import models.Product.Order;

public class TxtInputOutput<T> implements Serialization<T> {

	private Class<T> clas;
	public Class<T> getClas() {
		return clas;
	}
	
	public void setClas(Class<T> clas) {
		this.clas = clas;
	}

	public TxtInputOutput(Class<T> clas) {
		this.clas = clas;
	}
	
	@Override
	public void toFile(ArrayList<T> object, File file) {
		try {
			PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
			String out = getClas().getSimpleName() + "s:\r\n";
			ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(getClas().getMethods()).
									stream().filter(s -> s.getName().contains("get")).collect(Collectors.toList()));
			methods = (ArrayList<Method>)methods.stream().filter(s -> !s.getName().contains("getClass")).collect(Collectors.toList());
			Comparator<Method> cmp = new Comparator<Method>() {
			    @Override
			    public int compare(Method o1, Method o2) {
			    	Order or1 = o1.getAnnotation(Order.class);
	                Order or2 = o2.getAnnotation(Order.class);
	                if (or1 != null && or2 != null) 
	                    return or1.value() - or2.value();
	                return o1.getName().compareTo(o2.getName());
			    }
			};
			Collections.sort((List<Method>)methods, cmp);
			for(T o : object) {
				for(Method m: methods)
					out += m.invoke(o).toString() + "\r\n";
				out += "\r\n";
			}
			out = out.substring(0, out.length()-2);
			writer.write(out);
			writer.close();
		} catch (FileNotFoundException | IllegalAccessException  | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<T> fromFile(File file) throws Exception {
		ArrayList<T> result = new ArrayList<T>();
		BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
		ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(getClas().getMethods()).
				stream().filter(s -> s.getName().contains("set")).collect(Collectors.toList()));
		Comparator<Method> cmp = new Comparator<Method>() {
		    @Override
		    public int compare(Method o1, Method o2) {
		    	Order or1 = o1.getAnnotation(Order.class);
                Order or2 = o2.getAnnotation(Order.class);
                if (or1 != null && or2 != null) {
                    return or1.value() - or2.value();
                }
                return o1.getName().compareTo(o2.getName());
		    }
		};
		Collections.sort((List<Method>)methods, cmp);
		while(in.readLine() != null) {
			@SuppressWarnings("unchecked")
			T object = (T) Class.forName(getClas().getName()).newInstance(); 
			for(Method m : methods) {
				String line = in.readLine();
					if(m.toString().contains("float"))
						m.invoke(object, Float.valueOf(line));
					else if(m.toString().contains("LocalDate"))
						m.invoke(object, LocalDate.parse(line));
					else if(m.toString().contains("int"))
						m.invoke(object, Integer.valueOf(line));
					else
						m.invoke(object, line);
			}
			result.add(object);
		}
		in.close();
		return result;
	}
}

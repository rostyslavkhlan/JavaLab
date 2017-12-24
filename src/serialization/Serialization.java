package serialization;

import java.io.File;
import java.util.ArrayList;

public interface Serialization<T> {
	void toFile(ArrayList<T> object, File file);
	ArrayList<T> fromFile(File file) throws Exception;
}

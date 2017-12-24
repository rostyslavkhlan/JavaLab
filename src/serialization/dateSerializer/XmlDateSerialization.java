package serialization.dateSerializer;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XmlDateSerialization implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        LocalDate zdt = (LocalDate) source;
        writer.setValue(zdt.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return LocalDate.parse(reader.getValue(), DateTimeFormatter.ISO_DATE);
    }

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(LocalDate.class);
    }
}
package ots.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonFileParser implements FileParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String dataDirectoryPath;

    public JsonFileParser(String dataDirectoryPath) {
        this.dataDirectoryPath = dataDirectoryPath;
    }

    public void writeToFile(String fileName, Object object) {
        try {
            mapper.writeValue(getFile(fileName), object);
        } catch (Exception exception) {
            throw new RuntimeException("Error when writing to file " + fileName, exception);
        }
    }

    public <Type> List<Type> parseFileAsList(String fileName, Class<Type> clazz) {
        return parseFile(fileName, mapper.readerForListOf(clazz));
    }

    public <KeyType, ValueType> Map<KeyType, ValueType> parseFileAsMap(String filename, Class<KeyType> keyClass, Class<ValueType> valueClass) {
        return parseFile(filename, mapper.readerFor(mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass)));
    }

    private <Type> Type parseFile(String fileName, ObjectReader reader) {
        try {
            return reader.readValue(getFile(fileName));
        } catch (Exception exception) {
            throw new RuntimeException("Error while reading from file " + fileName, exception);
        }
    }

    private File getFile(String fileName) {
        return new File(String.format("%s/%s.json", dataDirectoryPath, fileName));
    }
}

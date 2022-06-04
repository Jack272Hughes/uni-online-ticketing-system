package ots.utils;

import java.util.List;
import java.util.Map;

public interface FileParser {
    void writeToFile(String fileName, Object object);
    <Type> List<Type> parseFileAsList(String fileName, Class<Type> clazz);
    <KeyType, ValueType> Map<KeyType, ValueType> parseFileAsMap(String filename, Class<KeyType> keyClass, Class<ValueType> valueClass);
}

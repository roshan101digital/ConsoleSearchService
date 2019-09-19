package com.roshan.techassessment.consolesearchservice.utils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Json utility class for search provider service
 */
public class JsonFileReader {

    /**
     * Gets the {@link Map<Object, LinkedTreeMap>} for given file name
     *
     * @param fileName the json file name
     * @return Map<Object, LinkedTreeMap> record data
     * @throws java.lang.Exception if error retrieving data
     *
     * @should return all available data
     */
    public static Map<Object, LinkedTreeMap> readJsonFileData(String fileName)
            throws Exception {

        File jsonFile = Paths.get(fileName).toFile();
        Type collectionType = new TypeToken<ArrayList<Object>>() {}.getType();
        ArrayList<LinkedTreeMap> dataObjects = new Gson().fromJson(new FileReader(jsonFile), collectionType);
        Map<Object, LinkedTreeMap> userMap = dataObjects.parallelStream().collect(
                Collectors.toMap(x -> x.get("_id"), x -> x));
        return userMap;
    }
}

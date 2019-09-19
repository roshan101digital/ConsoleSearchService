package com.roshan.techassessment.consolesearchservice;

import com.google.gson.internal.LinkedTreeMap;
import java.util.List;

/**
 * <code>HierarchicalSearchService </code> provides an interface for search provider implementations
 */
public interface HierarchicalSearchService {

    public LinkedTreeMap getRecordById(Object id);

    public List<LinkedTreeMap> hierarchicalSearch(final String term, final String value);

    public void printRecordData(final List<LinkedTreeMap> recordData);

    public void printSearchable();
}

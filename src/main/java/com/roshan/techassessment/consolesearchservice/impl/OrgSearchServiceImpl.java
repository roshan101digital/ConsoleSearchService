package com.roshan.techassessment.consolesearchservice.impl;

import com.google.gson.internal.LinkedTreeMap;
import com.roshan.techassessment.consolesearchservice.*;
import com.roshan.techassessment.consolesearchservice.utils.JsonFileReader;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import static com.roshan.techassessment.consolesearchservice.Application.console;

/**
 * Search provider implementation class for organization data. Implement {@link HierarchicalSearchService}
 */
public class OrgSearchServiceImpl implements HierarchicalSearchService {

    private static final String ORG_PROFILE_FILE = "organizations.json";
    private static OrgSearchServiceImpl singletonInstance;
    Map<Object, LinkedTreeMap> orgSearchData;
    Set searchable;

    /**
     * Initialize organization data from the file <code>json</code> file 'organizations.json'
     *
     * @should terminate the program if initialization failed
     */
    private OrgSearchServiceImpl() {
        try {
            orgSearchData = JsonFileReader.readJsonFileData(ORG_PROFILE_FILE);
            searchable = orgSearchData.entrySet()
                    .stream()
                    .limit(1)
                    .map(e -> ((LinkedTreeMap) e.getValue()).keySet())
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            console.format("Error occurred while initializing organization search provider: %s \n",
                    ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Gets singleton instance
     * @return organization implementation of {@link HierarchicalSearchService} implementation class 
     */
    public static HierarchicalSearchService getInstance() {
        if (null == singletonInstance) {
            singletonInstance = new OrgSearchServiceImpl();
        }
        return singletonInstance;
    }

    /**
     * Gets search record data if data exist for given id
     * 
     * @param id the search _id filed value
     * @return LinkedTreeMap, search record entries
     
     * @should return all search results
     * @should return null if no matching entry is found
     */
    @Override
    public LinkedTreeMap getRecordById(Object id) {
        LinkedTreeMap recordData = null;
        if (orgSearchData.containsKey(id)) {
            recordData = orgSearchData.get(id);
        }
        return recordData;
    }

    /**
     * Perform hierarchical search on organization data for given field and value input entries.
     * 
     * @param term the search field value
     * @param value the search value
     * @return LinkedTreeMap, search record entries
     
     * @should return all search results
     * @should return empty <code>List</code> if no matching entries found
     */
    @Override
    public List<LinkedTreeMap> hierarchicalSearch(String term, String value) {

        return orgSearchData.entrySet().parallelStream()
                .filter(e -> {
                    LinkedTreeMap user = e.getValue();
                    if (user.get(term) instanceof Double) {
                        return (Double.compare((Double) user.get(term), Double.parseDouble(value)) == 0);
                    } else if (user.get(term) instanceof String) {
                        Pattern p = Pattern.compile("\\b" + value + "\\b");
                        Matcher m = p.matcher((String) user.get(term));
                        return m.find();
                    } else if (user.get(term) instanceof ArrayList) {
                        return ((ArrayList) user.get(term)).contains(value);
                    }
                    return false;
                })
                .map(e -> (LinkedTreeMap) e.getValue())
                .collect(Collectors.toList());
    }

    /**
     * Print searchable fields for organization data
     */
    @Override
    public void printSearchable() {
        console.writer().println("\nSearch organization with");
        searchable.forEach((key) -> console.format("%s%n", key));
    }

    /**
     * Print data for given {@link LinkedTreeMap} organization record entries. Additionally it will search and
     * display related data from user and tickets as well
     * 
     * @param orgData the organization record entries
     *
     * @should print all list of organization results and related tickets and user attributes as well
     */
    @Override
    public void printRecordData(final List<LinkedTreeMap> orgData) {

        orgData.forEach(data -> {
            console.writer().println("\n");
            data.forEach((key, value) -> console.format("%-30.30s  %-30.30s%n", key, value));
            //get ticket subject value
            List<LinkedTreeMap> filteredData = TicketSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.organizationkey.getValue(), String.valueOf(data.get(SearchProvider.idkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(ticketData -> console.format("%-30.30s  %-30.30s%n", "ticket_" + ticketData.get(SearchProvider.idkey.getValue()),
                        ticketData.get(SearchProvider.subjectkey.getValue())));
            }

            //get organization name value
            filteredData = UserSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.organizationkey.getValue(), String.valueOf(data.get(SearchProvider.idkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(userData -> console.format("%-30.30s  %-30.30s%n", "user_" + userData.get(SearchProvider.idkey.getValue()),
                        userData.get(SearchProvider.namekey.getValue())));
            }
        }
        );
    }

}

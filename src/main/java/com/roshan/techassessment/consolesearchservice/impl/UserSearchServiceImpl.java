package com.roshan.techassessment.consolesearchservice.impl;

import com.google.gson.internal.LinkedTreeMap;
import com.roshan.techassessment.consolesearchservice.*;
import com.roshan.techassessment.consolesearchservice.utils.JsonFileReader;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import static com.roshan.techassessment.consolesearchservice.Application.console;

/**
 * Search provider implementation class for user data. Implement {@link HierarchicalSearchService}
 */
public class UserSearchServiceImpl implements HierarchicalSearchService {

    private static final String USER_PROFILE_FILE = "users.json";

    private static UserSearchServiceImpl singletonInstance;
    Map<Object, LinkedTreeMap> userSearchData;
    Set searchable;

    /**
     * Initialize user data from the file <code>json</code> file 'users.json'
     *
     * @should terminate the program if initialization failed
     */
    private UserSearchServiceImpl() {
        try {
            userSearchData = JsonFileReader.readJsonFileData(USER_PROFILE_FILE);
            searchable = userSearchData.entrySet()
                    .stream()
                    .limit(1)
                    .map(e -> ((LinkedTreeMap) e.getValue()).keySet())
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            console.format("Error occurred while initializing tickets search provider: %s \n",
                    ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Gets singleton instance
     * @return user implementation of {@link HierarchicalSearchService} implementation class 
     */
    public static HierarchicalSearchService getInstance() {
        if (null == singletonInstance) {
            singletonInstance = new UserSearchServiceImpl();
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
        if (userSearchData.containsKey(id)) {
            recordData = userSearchData.get(id);
        }
        return recordData;
    }

    /**
     * Perform hierarchical search on user data for given field and value input entries.
     * 
     * @param term the search field value
     * @param value the search value
     * @return LinkedTreeMap, search record entries
     
     * @should return all search results
     * @should return empty <code>List</code> if no matching entries found
     */
    @Override
    public List<LinkedTreeMap> hierarchicalSearch(String term, String value) {

        return userSearchData.entrySet().parallelStream()
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
     * Print searchable fields for user data
     */
    @Override
    public void printSearchable() {
        console.writer().println("\nSearch users with");
        searchable.forEach((key) -> console.format("%s%n", key));
    }

    /**
     * Print data for given {@link LinkedTreeMap} user record entries. Additionally it will search and
     * display related data from user and tickets as well
     * 
     * @param userData the user record entries
     *
     * @should print all list of user results and related tickets and user attributes as well
     */
    @Override
    public void printRecordData(final List<LinkedTreeMap> userData) {
        userData.forEach(data -> {
            console.writer().println("\n");
            data.forEach((key, value) -> console.format("%-30.30s  %-30.30s%n", key, value));

            //ticket data assignee ticket subject
            List<LinkedTreeMap> filteredData = TicketSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.assigneekey.getValue(), String.valueOf(data.get(SearchProvider.idkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(ticketData -> console.format("%-30.30s  %-30.30s%n", "assinee_ticket_" + ticketData.get(SearchProvider.idkey.getValue()),
                        ticketData.get(SearchProvider.subjectkey.getValue())));
            }
            //ticket data submitted ticket subject
            filteredData = TicketSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.submitterkey.getValue(), String.valueOf(data.get(SearchProvider.idkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(ticketData -> console.format("%-30.30s  %-30.30s%n", "submitted_ticket_" + ticketData.get(SearchProvider.idkey.getValue()),
                        ticketData.get(SearchProvider.subjectkey.getValue())));
            }

            //organization name
            filteredData = OrgSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.idkey.getValue(), String.valueOf(data.get(SearchProvider.organizationkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(orgData -> console.format("%-30.30s  %-30.30s%n", "organization_name" + orgData.get(SearchProvider.idkey.getValue()),
                        orgData.get(SearchProvider.namekey.getValue())));
            }
        }
        );
    }
}

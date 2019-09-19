package com.roshan.techassessment.consolesearchservice.impl;

import com.google.gson.internal.LinkedTreeMap;
import com.roshan.techassessment.consolesearchservice.*;
import com.roshan.techassessment.consolesearchservice.utils.JsonFileReader;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import static com.roshan.techassessment.consolesearchservice.Application.console;

/**
 * Search provider implementation class for ticket data. Implement {@link HierarchicalSearchService}
 */
public class TicketSearchServiceImpl implements HierarchicalSearchService {

    private static TicketSearchServiceImpl singletonInstance;
    Map<Object, LinkedTreeMap> ticketSearchData;
    private static final String TICKETS_PROFILE_FILE = "tickets.json";
    Set searchable;

     /**
     * Initialize ticket data from the file <code>json</code> file 'tickets.json'
     *
     * @should terminate the program if initialization failed
     */
    private TicketSearchServiceImpl() {
        try {
            ticketSearchData = JsonFileReader.readJsonFileData(TICKETS_PROFILE_FILE);
            searchable = ticketSearchData.entrySet()
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
     * @return ticket implementation of {@link HierarchicalSearchService} implementation class 
     */
    public static HierarchicalSearchService getInstance() {
        if (null == singletonInstance) {

            singletonInstance = new TicketSearchServiceImpl();

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
        if (ticketSearchData.containsKey(id)) {
            recordData = ticketSearchData.get(id);
        }
        return recordData;
    }

    /**
     * Perform hierarchical search on ticket data for given field and value input entries.
     * 
     * @param term the search field value
     * @param value the search value
     * @return LinkedTreeMap, search record entries
     
     * @should return all search results
     * @should return empty <code>List</code> if no matching entries found
     */
    @Override
    public List<LinkedTreeMap> hierarchicalSearch(String term, String value) {

        return ticketSearchData.entrySet().parallelStream()
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
     * Print searchable fields for ticket data
     */
    @Override
    public void printSearchable() {
        console.writer().println("\nSearch tickets with");
        searchable.forEach((key) -> console.format("%s%n", key));
    }

    /**
     * Print data for given {@link LinkedTreeMap} ticket record entries. Additionally it will search and
     * display related data from user and tickets as well
     * 
     * @param ticketData the ticket record entries
     *
     * @should print all list of ticket results and related tickets and user attributes as well
     */
    @Override
    public void printRecordData(final List<LinkedTreeMap> ticketData) {
        ticketData.forEach(data -> {
            console.writer().println("\n");
            data.forEach((key, value) -> console.format("%-30.30s  %-30.30s%n", key, value));

            //user data assignee name
            List<LinkedTreeMap> filteredData = UserSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.idkey.getValue(), String.valueOf(data.get(SearchProvider.assigneekey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(userData -> console.format("%-30.30s  %-30.30s%n", "assignee_" + userData.get(SearchProvider.idkey.getValue()),
                        userData.get(SearchProvider.namekey.getValue())));
            }
            //user data submitter name
            filteredData = UserSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.idkey.getValue(), String.valueOf(data.get(SearchProvider.submitterkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(userData -> console.format("%-30.30s  %-30.30s%n", "submitter_" + userData.get(SearchProvider.idkey.getValue()),
                        userData.get(SearchProvider.namekey.getValue())));
            }
            //ticket data organization name
            filteredData = OrgSearchServiceImpl.getInstance()
                    .hierarchicalSearch(SearchProvider.idkey.getValue(), String.valueOf(data.get(SearchProvider.organizationkey.getValue())));
            if (filteredData != null) {
                filteredData.forEach(orgData -> console.format("%-30.30s  %-30.30s%n", "organization_" + orgData.get(SearchProvider.idkey.getValue()),
                        orgData.get(SearchProvider.namekey.getValue())));
            }
        }
        );
    }
}

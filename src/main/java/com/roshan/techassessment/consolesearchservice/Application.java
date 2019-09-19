package com.roshan.techassessment.consolesearchservice;

import com.roshan.techassessment.consolesearchservice.impl.*;
import com.google.gson.internal.LinkedTreeMap;
import java.io.*;
import java.util.List;

/**
 * Core standalone application for running the console based search service
 */
public class Application {

    public static Console console = System.console();

    public static void main(String[] args) {
        if (console == null) {
            System.out.println("Console is not supported");
            System.exit(1);
        }
        displayMainView();
    }

    /**
     * Display the initial screen for the user. Provide option to navigate to the search menu or print the
     * searchable fields available for each search provider
     */
    public static void displayMainView() {
        String option = consoleRead(""
                + "\n\n"
                + "Type 'Quit' to exit at any time, Press 'Enter' to continue \n\n"
                + "\t Select search options:\n\n"
                + "\t * Press 1 to search\n"
                + "\t * Press 2 to view a list of searchable fields\n"
                + "\t * Type 'quit' to exit\n");

        switch (option) {
            case "1":
                displaySearchView();
                break;
            case "2":
                printSearchable();
                break;
            default:
                console.format("Invalid option entered : %s \n", option);
                clearScreen();
                break;
        }

    }

    /**
     * Display search view for for the available search data providers. Search will be provided for given field
     * and value if valid search provider selected
     */
    public static void displaySearchView() {

        //Display the search options menu
        String searchType = consoleRead("\n"
                + "Select 1) Users or 2) Tickets or 3) Organizations)\n");
        String term = consoleRead("Enter search term\n");
        String searchValue = consoleRead("Enter search value\n");

        try {
            //Search for the provider implementation for given search type
            HierarchicalSearchService hierarchicalSearchService = getSearchProvider(searchType);
            if (hierarchicalSearchService != null) {

                //Perform the hierarchical searchService for provider field and value user inputs
                List<LinkedTreeMap> filteredData = hierarchicalSearchService.hierarchicalSearch(term, searchValue);
                if (!filteredData.isEmpty()) {
                    hierarchicalSearchService.printRecordData(filteredData);
                } else {
                    console.format("No data found for the provided criteria type %s, term %s, value %s"
                            + "\n", searchType, term, searchValue);
                }
            }
        } catch (Exception ex) {
            console.format("Error occurred while searching data: %s : %s \n",
                    searchType, ex.getMessage());
        }
        //Continue to new search
        consoleRead("");
        clearScreen();
    }

    /**
     * Gets the {@link HierarchicalSearchService} for given search provider input
     *
     * @param searchType the risk search provider type
     * @return HierarchicalSearchService implementation class
     *
     * @should return the matching matching implementation class
     * @should return null if no matching entry is found *
     */
    private static HierarchicalSearchService getSearchProvider(final String searchType) {
        HierarchicalSearchService hierarchicalSearchService = null;

        if (SearchProvider.organizations.getValue().equals(searchType)) {
            hierarchicalSearchService = OrgSearchServiceImpl.getInstance();
        } else if (SearchProvider.tickets.getValue().equals(searchType)) {
            hierarchicalSearchService = TicketSearchServiceImpl.getInstance();
        } else if (SearchProvider.users.getValue().equals(searchType)) {
            hierarchicalSearchService = UserSearchServiceImpl.getInstance();
        } else {
            console.format("Invalid option entered : %s \n", searchType);
        }
        return hierarchicalSearchService;
    }

    /**
     * Print the searchable fields available for each search provider implementation
     */
    public static void printSearchable() {

        //Print searchable fields for each provider implementation
        OrgSearchServiceImpl.getInstance().printSearchable();
        TicketSearchServiceImpl.getInstance().printSearchable();
        UserSearchServiceImpl.getInstance().printSearchable();
        consoleRead("");
        clearScreen();
    }

    /**
     * Read the console user input. Validate the user input for termination indicator is equal to 'quit'
     *
     * @param userInput entered in console
     * @return userInput if user not opted for quit
     *
     * @should return the user input data if entry value is not 'quit'
     * @should terminate the program if user entered 'quit'
     */
    public static String consoleRead(final String userInput) {
        String option = console.readLine(userInput);
        if (option.equalsIgnoreCase("quit")) {
            System.exit(0);
        }
        return option;
    }

    /**
     * Clear the console for user input
     */
    public static void clearScreen() {
        console.printf("\033[H\033[2J");
        console.flush();
        displayMainView();
    }
}

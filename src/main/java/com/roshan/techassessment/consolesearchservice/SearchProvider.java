package com.roshan.techassessment.consolesearchservice;

/**
 * Search provider constant for console search provider service
 */
public enum SearchProvider {

    organizations("3"),
    tickets("2"),
    users("1"),
    idkey("_id"),
    subjectkey("subject"),
    namekey("name"),
    assigneekey("assignee_id"),
    submitterkey("submitter_id"),
    organizationkey("organization_id");

    private String value;

    SearchProvider(String param) {
        this.value = param;
    }

    public String getValue() {
        return value;
    }
}

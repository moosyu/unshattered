package io.github.moosyu.screens.states;

public class StatsScreenState {
    private String selectedTab;

    public StatsScreenState() {
        this.selectedTab = "stats";
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
}

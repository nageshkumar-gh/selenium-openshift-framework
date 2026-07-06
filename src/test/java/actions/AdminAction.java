package actions;

import pages.AdminPage;

public class AdminAction {

    private final AdminPage adminPage = new AdminPage();

    public void searchUserByAdmin(String username) {
        adminPage.enterUsernameToSearch(username);
        adminPage.clickAdminSearch();
    }
    public boolean verifyAdminIsSearched(String username) {
        return adminPage.getResultFromTable(username);
    }
}


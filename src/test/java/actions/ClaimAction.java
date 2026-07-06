package actions;

import pages.ClaimPage;

public class ClaimAction {

    private final ClaimPage claimPage = new ClaimPage();

    public void createNewClaim(String empName, String event, String currency, String remarks) {
        claimPage.clickAssignClaimButton();
        claimPage.enterEmployeeName(empName);
        claimPage.selectOption(empName);
        claimPage.clickEventListButton();
        claimPage.selectOption(event);
        claimPage.clickCurrentListButton();
        claimPage.selectOption(currency);
        claimPage.enterRemarks(remarks);
        claimPage.clickCreateClaimButton();
    }
    public boolean isClaimSubmittedSuccessfully(String successMsg) {
        return claimPage.getSuccessMessage(successMsg);

    }
}


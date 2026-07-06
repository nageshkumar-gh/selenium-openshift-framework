package actions;

import pages.BuzzPage;

public class BuzzAction {

    private final BuzzPage buzzPage = new BuzzPage();

    public void sharePost(String message){
        buzzPage.writePost(message);
        buzzPage.clickPost();
    }
    public boolean whatPosted(String message) {
        return buzzPage.getPostText(message);
    }
}


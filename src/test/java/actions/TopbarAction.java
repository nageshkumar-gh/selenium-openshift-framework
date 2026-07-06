package actions;

import pages.TopbarPage;

public class TopbarAction extends TopbarPage {

     public boolean isNavigatedToModule(String title) {
         return getTopbarTitle(title);
     }
}

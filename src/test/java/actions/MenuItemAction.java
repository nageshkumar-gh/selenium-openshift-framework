package actions;

import pages.MenuItemPage;

public class MenuItemAction extends MenuItemPage {

    public void navigateToModule(String menuItemName) {
        selectMenuItem(menuItemName);
    }
}

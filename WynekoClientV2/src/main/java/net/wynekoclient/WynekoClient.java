package net.wynekoclient;

import net.wynekoclient.util.menu.BindsMenu;

public class WynekoClient{
    static boolean Dev = true;
    public static void Start(){
        if (Dev){
            DevWindow.init();
            BindsMenu.MacroManager.load();
        }
    }
}

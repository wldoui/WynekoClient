package net.wynekoclient;

public class WynekoClient {
    static boolean Dev = true;
    public static void Start(){
        if (Dev){
            DevWindow.CreateWindow();
        }
    }
}

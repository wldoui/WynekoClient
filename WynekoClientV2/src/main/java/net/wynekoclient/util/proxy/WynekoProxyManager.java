package net.wynekoclient.util.proxy;

import net.wynekoclient.DevWindow;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class WynekoProxyManager {

    public static boolean ProxyEnabled = DevWindow.ProxyEnabled;
    public static String Host = DevWindow.GetHost();
    public static int Port = DevWindow.GetPort();
    public static String Type = DevWindow.GetType();

    public static Proxy getProxy() {
        if (!ProxyEnabled) return Proxy.NO_PROXY;
        Proxy.Type t = Type.equalsIgnoreCase("HTTP") ? Proxy.Type.HTTP : Proxy.Type.SOCKS;
        return new Proxy(t, new InetSocketAddress(Host, Port));
    }

    public static String getHost() {
        return Host;
    }

    public static int getPort() {
        return Port;
    }

    public static boolean isEnabled() {
        return ProxyEnabled;
    }

    public static boolean isSocks5() {
        return Type.equalsIgnoreCase("SOCKS5");
    }
}
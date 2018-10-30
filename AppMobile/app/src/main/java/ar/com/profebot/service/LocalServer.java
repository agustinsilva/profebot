package ar.com.profebot.service;

public class LocalServer {
    private static String localServerIP = "192.168.1.3";

    public static String getLocalUrl() {
        return localServerIP != null ? "http://" + localServerIP + ":8080/more/practice" : "";
    }

    public static void setLocalUrl(String localServerIP) {
        LocalServer.localServerIP = localServerIP;
    }
}

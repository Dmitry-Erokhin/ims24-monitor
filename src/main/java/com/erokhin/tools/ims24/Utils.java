package com.erokhin.tools.ims24;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
public class Utils {

    public static String concatURL(String base, String path) {

        if (base.endsWith("/")) {
            base = base.substring(0, base.length()-1);
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return base + "/" + path;
    }

    public static String getBaseUrl(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        String portString = url.getPort() == -1 ? "" : ":" + url.getPort();
        return url.getProtocol() + "://" + url.getHost() + portString;
    }
}

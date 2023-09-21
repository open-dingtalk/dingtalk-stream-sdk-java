package com.dingtalk.open.app.api.util;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author feiyin
 * @date 2023/2/9
 */
public class IpUtils {

    private static volatile String LOCAL = null;

    static {
        try {
            InetAddress address = IpUtils.getIPv4();
            if (address != null) {
                LOCAL = address.getHostAddress();
            }
        } catch (Exception e) {
        }
    }

    public static String getLocalIP() {
        return LOCAL;
    }

    private static InetAddress getIPv4() throws SocketException, UnknownHostException {
        final ArrayList<InetAddress> v4Interfaces = new ArrayList<>();

        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface interfaces = enumeration.nextElement();
            Enumeration<InetAddress> addresses = interfaces.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address.isLoopbackAddress()) {
                    continue;
                }

                if (address instanceof Inet6Address) {
                    continue;
                }

                v4Interfaces.add(address);
            }
        }

        int v4InterfacesSize = v4Interfaces.size();
        if (v4InterfacesSize == 0) {
            return null;
        }

        for (InetAddress address : v4Interfaces) {

            final String host = address.getHostAddress();
            if (host == null) {
                continue;
            }
            if (host.startsWith("127.0") || host.startsWith("192.168")) {
                continue;
            }

            return address;
        }

        return v4Interfaces.get(v4InterfacesSize - 1);
    }
}

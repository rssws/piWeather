package wang.zhongpin.pi.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public final class Utils {
    private static final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private static final Pattern pattern = Pattern.compile(IPV4_PATTERN);

    private static boolean isValidIpV4(final String s) {
        if (s == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("True-Client-IP");
            if (isValidIpV4(remoteAddr)) return remoteAddr;
            remoteAddr = request.getHeader("CF-Connecting-IP");
            if (isValidIpV4(remoteAddr)) return remoteAddr;
            remoteAddr = request.getHeader("X-FORWARDED-FOR").split(",")[0];
            if (isValidIpV4(remoteAddr)) return remoteAddr;
            remoteAddr = request.getRemoteAddr();
            if (isValidIpV4(remoteAddr)) return remoteAddr;
            remoteAddr = "";
        }
        return remoteAddr;
    }
}

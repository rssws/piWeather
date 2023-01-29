package wang.zhongpin.pi.server;

import org.apache.commons.validator.routines.InetAddressValidator;
import jakarta.servlet.http.HttpServletRequest;

public final class Utils {
    private static boolean isValidAddr(final String s) {
        InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        return inetAddressValidator.isValid(s);
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("CF-Connecting-IP");
            if (isValidAddr(remoteAddr)) return remoteAddr;
            remoteAddr = request.getHeader("X-FORWARDED-FOR").split(",")[0];
            if (isValidAddr(remoteAddr)) return remoteAddr;
            remoteAddr = request.getRemoteAddr();
            if (isValidAddr(remoteAddr)) return remoteAddr;
            remoteAddr = "";
        }
        return remoteAddr;
    }
}

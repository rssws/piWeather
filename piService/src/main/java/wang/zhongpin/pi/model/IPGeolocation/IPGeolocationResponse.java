package wang.zhongpin.pi.model.IPGeolocation;

import wang.zhongpin.pi.model.Response;
import wang.zhongpin.pi.model.ResponseStatus;

public class IPGeolocationResponse extends Response {
    private IPGeolocation ipGeolocation;

    public IPGeolocationResponse(ResponseStatus responseStatus, IPGeolocation ipGeolocation) {
        this(responseStatus, "", ipGeolocation);
    }

    public IPGeolocationResponse(ResponseStatus responseStatus, String responseMessage, IPGeolocation ipGeolocation) {
        super(responseStatus, responseMessage);
        this.ipGeolocation = ipGeolocation;
    }

    public IPGeolocation getIpGeolocation() {
        return ipGeolocation;
    }

    public void setIpGeolocation(IPGeolocation ipGeolocation) {
        this.ipGeolocation = ipGeolocation;
    }
}

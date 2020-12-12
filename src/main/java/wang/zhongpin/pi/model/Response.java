package wang.zhongpin.pi.model;


public class Response {
    public ResponseStatus responseStatus;
    public String responseMessage;

    public Response(ResponseStatus responseStatus, String responseMessage) {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }
}

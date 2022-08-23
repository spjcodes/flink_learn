package cn.jiayeli.streamingWebPlatform.common.common.response;

public enum ResponseEnums implements CommonResponse {

    SUCCESS(200000, ""),
    TIMEOUT(300000, "timeout"),
    UNKNOWN_ERROR(500000, "unknown error.")
    ;


    private int code;

    private String desc;

    ResponseEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getResponseCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }
}

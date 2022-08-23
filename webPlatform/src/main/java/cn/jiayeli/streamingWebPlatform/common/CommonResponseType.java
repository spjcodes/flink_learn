package cn.jiayeli.streamingWebPlatform.common;

import cn.jiayeli.streamingWebPlatform.common.common.response.ResponseEnums;

import java.util.Objects;

public class CommonResponseType {

    private int statusCode;

    private String desc;

    private Object result;

    public CommonResponseType() {
    }


    public static CommonResponseType ok(Object result) {
        return create(200, result);
    }

    public static CommonResponseType create(int statusCode, Object result) {
        CommonResponseType commonResponseType = new CommonResponseType();
        commonResponseType.setStatus(statusCode);
        commonResponseType.setResult(result);
       return commonResponseType;
    }

    public static CommonResponseType create(int statusCode, String desc, Object result) {
        CommonResponseType commonResponseType = new CommonResponseType();
        commonResponseType.setStatus(statusCode);
        commonResponseType.setDesc(desc);
        commonResponseType.setResult(result);
        return commonResponseType;
    }

    public static CommonResponseType error(ResponseEnums error) {
        return create(error.getResponseCode(), error.getDesc(), null);
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResult (Object result) {
        this.result = result;
    }

    public int getStatus() {
        return statusCode;
    }

    public Object getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonResponseType that = (CommonResponseType) o;
        return statusCode == that.statusCode && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, result);
    }

}

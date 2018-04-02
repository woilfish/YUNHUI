package com.loopj.common.exception;

/**
 * 交易异常，服务端返回交易非成功状态的异常
 * Created by xyz on 14-1-2.
 */
public class TradeException extends BaseException{

    //交易错误码，后台返回
    private String tradeCode ="";

    protected String errorMessage = "";

    public TradeException(String tradeCode, String errorMessage){
        super(errorMessage);
        this.errorMessage   = errorMessage;
        this.tradeCode      = tradeCode;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}

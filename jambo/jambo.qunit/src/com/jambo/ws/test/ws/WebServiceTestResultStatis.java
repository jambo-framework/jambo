package com.jambo.ws.test.ws;

/**
 * WebService测试结果统计
 * User: 彭宙硕
 * Date: 13-8-30
 * Time: 下午3:52
 */
public class WebServiceTestResultStatis {

    private String modelName;
    private int successCount;
    private int failureCount;

    public void increseSuccessCount(){
        this.successCount++;
    }

    public void increaseFailureCount(){
        this.failureCount++;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getTotalCount(){
        return this.getSuccessCount() + this.getFailureCount();
    }
}

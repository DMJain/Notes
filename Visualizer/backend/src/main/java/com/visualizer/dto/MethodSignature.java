package com.visualizer.dto;

import java.util.List;

public class MethodSignature {
    private String methodName;
    private String returnType;
    private List<ParameterInfo> parameters;

    public MethodSignature(String methodName, String returnType, List<ParameterInfo> parameters) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
    }
}

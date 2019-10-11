package com.server.service.function;

public class FunctionFactory {
    private static final FunctionFactory INSTANCE = new FunctionFactory();

    public static FunctionFactory getInstance() {
        return INSTANCE;
    }
    public Function getFunction(String functionType) {
        if (functionType == null) {
            return null;
        }
        if (functionType.equalsIgnoreCase("UPLOAD JSP")) {
            return new UploadJspFunction();

        } else if (functionType.equalsIgnoreCase("UPLOAD CLASS")) {
//            return new UploadJarFunction();

        } else if (functionType.equalsIgnoreCase("UPLOAD JAR")) {
            return new UploadJarFunction();

        } else if (functionType.equalsIgnoreCase("CLEAN BOILER")) {
            return new CleanBoilerFunction();

        } else if (functionType.equalsIgnoreCase("RESTART SERVER")) {
            return new RestartServerFunction();
        }
        return null;
    }
}

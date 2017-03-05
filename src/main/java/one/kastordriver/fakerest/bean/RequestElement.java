package one.kastordriver.fakerest.bean;

public enum RequestElement {
    ip((condition, request, binding) -> {
        final String elementName = "@" + RequestElement.ip.name();
        final String replacedElementName = "_" + RequestElement.ip.name();
        final String elementValue = request.ip();
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(elementName, replacedElementName);
    }),
    port((condition, request, binding) -> {
        final String elementName = "@" + RequestElement.port.name();
        final String replacedElementName = "_" + RequestElement.port.name();
        final int elementValue = request.port();
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(elementName, replacedElementName);
    }),
    contentLength((condition, request, binding) -> {
        final String elementName = "@" + RequestElement.contentLength.name();
        final String replacedElementName = "_" + RequestElement.contentLength.name();
        final int elementValue = request.contentLength();
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(elementName, replacedElementName);
    }),
    cookie((condition, request, binding) -> {
        final String firstElementPart = "@" + RequestElement.cookie.name() + "(";
        final String lastElementPart = ")";
        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(lastElementPart, startIndex);
        final String cookieName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart.replace("(", "\\(") + cookieName + lastElementPart.replace(")", "\\)");

        final String replacedElementName = "_" + RequestElement.cookie.name() + cookieName;
        final String elementValue = request.cookie(cookieName);
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(fullElementName, replacedElementName);
    }),
    header((condition, request, binding) -> {
        final String firstElementPart = "@" + RequestElement.header.name() + "(";
        final String lastElementPart = ")";
        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(lastElementPart, startIndex);
        final String headerName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart.replace("(", "\\(") + headerName + lastElementPart.replace(")", "\\)");

        final String replacedElementName = "_" + RequestElement.header.name() + headerName;
        final String elementValue = request.headers(headerName);
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(fullElementName, replacedElementName);
    });

    private RequestElementProcessor processCondition;

    RequestElement(RequestElementProcessor requestElementProcessor) {
        this.processCondition = requestElementProcessor;
    }

    public boolean containedInCondition(String condition) {
        return condition.indexOf("@" + this.name()) != -1;
    }

    public RequestElementProcessor getProcessCondition() {
        return processCondition;
    }
}

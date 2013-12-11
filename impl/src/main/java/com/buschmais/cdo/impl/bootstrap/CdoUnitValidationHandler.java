package com.buschmais.cdo.impl.bootstrap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class CdoUnitValidationHandler implements ValidationEventHandler {

    private List<String> errorMessages = new ArrayList<>();

    @Override
    public boolean handleEvent(ValidationEvent event) {
        if (event.getSeverity() == ValidationEvent.ERROR || event.getSeverity() == ValidationEvent.FATAL_ERROR) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("line ");
            stringBuilder.append(event.getLocator().getLineNumber());
            stringBuilder.append(": ");
            stringBuilder.append(event.getMessage());
            this.errorMessages.add(stringBuilder.toString());
            return true;
        } else {
            //ignore validation warnings
            return true;
        }
    }

    public boolean passesValidation() {
        return this.errorMessages.size() == 0;
    }

    public String getValidationMessages() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String errorMessage : errorMessages) {
            stringBuilder.append(errorMessage);
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}

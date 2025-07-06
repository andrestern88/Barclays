package com.example.barclays.domain.enums;

public enum SortCode {

    CENTRAL_LONDON("10-10-10");

    public final String label;

    private SortCode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}

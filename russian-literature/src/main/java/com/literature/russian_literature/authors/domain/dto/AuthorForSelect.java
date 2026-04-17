package com.literature.russian_literature.authors.domain.dto;

public record AuthorForSelect(
        Long id,
        String lastName,
        String firstName,
        String middleName
) {
    public String getFullName() {
        return lastName + " " + firstName + (middleName != null ? " " + middleName : "");
    }

    public String getShortName() {
        return lastName + " " +
                firstName.charAt(0) + "." +
                (middleName != null ? middleName.charAt(0) + "." : "");
    }
}

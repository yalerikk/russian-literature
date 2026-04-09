package com.literature.russian_literature.authors.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "authors")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName; // отчество

    private LocalDate birthDate;
    private LocalDate deathDate;

    @Column(length = 2000)
    private String biography;

    private String photoUrl;

    /**
     * Полное ФИО автора
     */
    public String getFullName() {
        return lastName + " " + firstName +
                (middleName != null ? " " + middleName : "");
    }

    /**
     * Краткое ФИО (для отображения в списках)
     */
    public String getShortName() {
        return lastName + " " +
                firstName.charAt(0) + "." +
                (middleName != null ? middleName.charAt(0) + "." : "");
    }

    /**
     * Инициалы
     */
    public String getInitials() {
        return firstName.charAt(0) + "." +
                (middleName != null ? middleName.charAt(0) + "." : "") +
                " " + lastName;
    }
}

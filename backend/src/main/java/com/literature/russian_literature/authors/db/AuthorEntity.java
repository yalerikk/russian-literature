package com.literature.russian_literature.authors.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

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

    @Formula("(SELECT COUNT(*) FROM books b WHERE b.author_id = id)")
    private int bookCount;

    public AuthorEntity(Long id, String firstName, String lastName, String middleName,
                        LocalDate birthDate, LocalDate deathDate, String biography, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.biography = biography;
        this.photoUrl = photoUrl;
        // bookCount вычисляется формулой
    }

    public String getFullName() {
        return lastName + " " + firstName +
                (middleName != null ? " " + middleName : "");
    }

    public String getShortName() {
        return lastName + " " +
                firstName.charAt(0) + "." +
                (middleName != null ? middleName.charAt(0) + "." : "");
    }
}

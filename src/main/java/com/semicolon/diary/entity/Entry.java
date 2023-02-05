package com.semicolon.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;
    private String title;
    private String body;
    private LocalDateTime date = LocalDateTime.now();
    @ManyToOne
    User user;

    public Entry(String title, String body, User user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

}

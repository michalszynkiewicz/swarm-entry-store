package com.github.michalszynkiewicz.entrystore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/26/17
 * Time: 10:46 AM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Entry {
    @GeneratedValue
    @Id
    private Integer id;
    private String author;
    private String content;

    public Entry(String author, String content) {
        this.author = author;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

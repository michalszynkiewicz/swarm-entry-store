package com.github.michalszynkiewicz.entrystore.endpoint;

import com.github.michalszynkiewicz.entrystore.model.Entry;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/26/17
 * Time: 10:54 AM
 */
public class EntryDto {
    private Integer id;
    private String author;
    private String content;

    public EntryDto() {
    }

    public EntryDto(Entry entry) {
        id = entry.getId();
        author = entry.getAuthor();
        content = entry.getContent();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Entry toEntry() {
        Entry entry = new Entry();
        entry.setAuthor(author);
        entry.setContent(content);
        return entry;
    }
}

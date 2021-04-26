package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍基本情報格納DTO
 */
@Configuration
@Data
public class BookInfo {

    private int bookId;

    private String title;

    private String author;

    private String publisher;

    private String publish_date;

    private String thumbnail_url;

    public BookInfo() {

    }

    // コンストラクta
    public BookInfo(int bookId, String title, String author, String publisher, String publish_date,
            String thumbnail_url) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publish_date = publish_date;
        this.thumbnail_url = thumbnail_url;


    }

}
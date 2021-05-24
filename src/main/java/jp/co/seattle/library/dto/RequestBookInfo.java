package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍基本情報格納DTO
 */
@Configuration
@Data
public class RequestBookInfo {

    private String requestTitle;

    private int counts;

    public RequestBookInfo() {

    }

    // コンストラクta
    public RequestBookInfo(String requestTitle, int counts) {

        this.requestTitle = requestTitle;
        this.counts = counts;
    }

}
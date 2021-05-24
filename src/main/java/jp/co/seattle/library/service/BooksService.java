package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.dto.RequestBookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;
import jp.co.seattle.library.rowMapper.RequestBookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        // 取得したい情報は、書籍ID、書籍名、著者名、出版社名、出版日、サムネイル画像
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }


    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        //dtoのBookDetailsInfoとrowMapperのBookDetailsInfoRowMapperを紐づけている
        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
        return bookDetailsInfo;

    }

    /**
     * 入力された書籍名を検索
     * 結果に紐づく書籍詳細情報を取得する
     *
     * @param searchTitle 書籍名
     * @return 書籍情報
     */
    public List<BookInfo> getMatchBooks(String searchTitle) {
        List<BookInfo> getSearchBookList = jdbcTemplate.query(
                "SELECT * FROM books where title like '"
                        + searchTitle + "' or title like '%" + searchTitle + "%' order by title='" + searchTitle
                        + "' DESC",
                new BookInfoRowMapper());
        return getSearchBookList;

    }


    /**
     * 書籍IDに紐づく書籍が貸し出し中であることを取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public String getLendingBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT Lend_status FROM lending where BOOK_ID ="
                + bookId;
        return jdbcTemplate.queryForObject(sql, String.class);

    }



    //一番大きいid（＝一番最近追加した本のid）をBDから取得
    //取得した結果を指定したクラスで返す

    public int getBookId() {
        String sql = "SELECT MAX(id) FROM books";
        return jdbcTemplate.queryForObject(sql, Integer.class);

    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title,description,author,publisher,publish_date,thumbnail_name,thumbnail_url,isbn,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','"
                + bookInfo.getDescription() + "','"
                + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getIsbn() + "',"
                + "sysdate(),"
                + "sysdate())";

        jdbcTemplate.update(sql);
    }

    /**
     * リクエストされた書籍が追加された際に、
     * リクエストテーブルから同じタイトルを削除する
     *
     * @param bookId 書籍ID
     */

    public void deleteRequestedBook(BookDetailsInfo bookInfo) {

        String sql = "DELETE FROM request where requestTitle ='" + bookInfo.getTitle() + "'";
        jdbcTemplate.update(sql);
    }


    /**
     * 書籍を更新する
     *
     * @param bookInfo 書籍情報
     */
    public void updateBook(BookDetailsInfo bookInfo) {

        String sql = "UPDATE books SET "
                + "title='" + bookInfo.getTitle() + "',"
                + "author='" + bookInfo.getAuthor() + "',"
                + "publisher='" + bookInfo.getPublisher() + "',"
                + "publish_date='" + bookInfo.getPublishDate() + "',"
                + "isbn='" + bookInfo.getIsbn() + "',"
                + "description='" + bookInfo.getDescription() + "',"
                + "upd_date=" + "sysdate()"
                + "WHERE id="
                + bookInfo.getBookId() + ";";

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を削除する
     *
     * @param bookId 書籍ID
     */

    public void deleteBook(int bookId) {

        String sql = "DELETE FROM books where id =" + bookId;
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍貸し出し中に設定する
     *
     * @param bookId 書籍ID
     */

    public void lendingBook(int bookId) {
        String sql = "INSERT INTO lending(BOOK_ID) values(" + bookId + ");";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍貸し出し中であるかチェックする
     *
     * @param bookId 書籍ID
     */

    public int lendingBookCountCheck(int bookId) {
        String sql = "select count(BOOK_ID) from lending where BOOK_ID =" + bookId + ";";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 書籍を返却する
     *
     * @param bookId 書籍ID
     */

    public void returnBook(int bookId) {
        String sql = "DELETE FROM lending where BOOK_ID =" + bookId;
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍のリクエストをrequestTBLに追加する
     *
     * @param requestTitle リクエストタイトル
     */

    public void requestBook(String requestTitle) {
        String sql = "INSERT INTO request(requestTitle) values('" + requestTitle + "');";
        jdbcTemplate.update(sql);
    }

    /**
     * すでにリクエストされている本の冊数カウントを１増やす
     *
     * @param requestTitle リクエストタイトル
     */

    public void requestCounts(String requestTitle) {
        String sql = "UPDATE request SET counts = counts +1 where requestTitle= '" + requestTitle + "';";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍のリクエストをリクエストの多い順に5つ取得する
     * 
     * @return 
     */

    public List<RequestBookInfo> getRequestList() {

        String sql = "SELECT requestTitle, counts FROM request order by counts DESC limit 5;";

        try {
            List<RequestBookInfo> RequestedBookInfo = jdbcTemplate.query(sql, new RequestBookInfoRowMapper());
            return (List<RequestBookInfo>) RequestedBookInfo;

    } catch (IncorrectResultSizeDataAccessException r) {
        return null;
    }
    }
}
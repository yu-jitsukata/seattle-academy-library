package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.RequestBookInfo;

@Configuration
public class RequestBookInfoRowMapper implements RowMapper<RequestBookInfo> {

    @Override
    public RequestBookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        RequestBookInfo RequestBookInfo = new RequestBookInfo();

        // RequestBookInfoの項目と、取得した結果(rs)のカラムをマッピングする

        RequestBookInfo.setRequestTitle(rs.getString("requestTitle"));
        RequestBookInfo.setCounts(rs.getInt("counts"));

        return RequestBookInfo;
    }

}
package eu.profinit.stm.typeHandler;

import eu.profinit.stm.model.user.SocialProviderEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SocialEnumTypeHandler extends BaseTypeHandler<SocialProviderEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SocialProviderEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public SocialProviderEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return SocialProviderEnum.getId(rs.getString(columnName));
    }

    @Override
    public SocialProviderEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return SocialProviderEnum.getId(rs.getString(columnIndex));
    }

    @Override
    public SocialProviderEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return SocialProviderEnum.getId(cs.getString(columnIndex));
    }
}



package org.tbm.common.access;

import org.tbm.common.annotation.Column;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jason.Xia on 17/5/31.
 */
public class SqlAssembler {


    public static List<Object> convertResult(ResultSet rawSet, Class clazz) throws SQLException,
            IllegalAccessException, InstantiationException {
        List<Object> result = new ArrayList<>();
        ResultSetMetaData metaData = rawSet.getMetaData();
        Set<String/*column name*/> columns = new HashSet<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.add(metaData.getColumnName(i));
        }


        while (rawSet.next()) {
            Field[] fields = clazz.getDeclaredFields();
            Object instance = clazz.newInstance();
            for (Field field : fields) {
                Column annotation = field.getAnnotation(Column.class);
                if (null == annotation) {
                    continue;
                }

                if (!columns.contains(annotation.value())) {
                    continue;
                }

                field.setAccessible(true);
                field.set(instance, rawSet.getObject(annotation.value()));
            }

            result.add(instance);
        }

        return result;
    }

    public static PreparedStatement build(PreparedStatement ps, List<Object> values) throws SQLException {
        if (null == values || 0 == values.size()) {
            return null;
        }

        for (int i = 1; i <= values.size(); i++) {
            setValue(i, ps, values.get(i - 1));
        }

        return ps;
    }

    private static void setValue(int index, PreparedStatement ps, Object value) throws SQLException {
        if (value instanceof Byte) {
            ps.setByte(index, (byte) value);
        } else if (value instanceof Short) {
            ps.setShort(index, (short) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (int) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (long) value);
        } else if (value instanceof Float) {
            ps.setFloat(index, (float) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (double) value);
        } else if (value instanceof String) {
            ps.setString(index, String.valueOf(value));
        } else {
            throw new IllegalArgumentException("no match type");
        }
    }

    public static PreparedStatement buildBatch(PreparedStatement ps, List<Object> values) {
        return null;
    }
}

package org.tbm.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class JsonConfigReader {
    public static <T> Map<String, T> readerObject(String path, Type clazz) throws FileNotFoundException {
        JSONReader reader = new JSONReader(new FileReader(path));
        Map<String, T> result = new HashMap<>();
        reader.startObject();
        while (reader.hasNext()) {
            String key = reader.readString();
            reader.startObject();
            JSONObject object = new JSONObject();
            while (reader.hasNext()) {
                object.put(reader.readString(), reader.readObject().toString());
            }

            reader.endObject();
            T value = JSONObject.parseObject(object.toJSONString(), clazz);
            result.put(key, value);
        }

        reader.endObject();

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> readerArray(String path, Type clazz) throws FileNotFoundException {
        JSONReader reader = new JSONReader(new FileReader(path));
        List<T> result = new ArrayList<>();

        reader.startArray();
        while (reader.hasNext()) {
            result.add((T) JSONObject.parseObject(reader.readObject().toString(), clazz));
        }

        reader.endArray();
        return result;
    }

}

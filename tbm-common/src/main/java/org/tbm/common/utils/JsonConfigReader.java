package org.tbm.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.tbm.common.access.Operation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
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

    public static void main(String[] args) throws FileNotFoundException {
        URL resource = JsonConfigReader.class.getResource("/operation.json");
        Map<String, Operation> map = JsonConfigReader.readerObject(resource.getFile(), Operation.class);
        System.out.println(map);
    }
}

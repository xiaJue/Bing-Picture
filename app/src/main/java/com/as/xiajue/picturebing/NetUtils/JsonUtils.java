package com.as.xiajue.picturebing.NetUtils;

import com.as.xiajue.picturebing.model.HomeItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.as.xiajue.picturebing.model.HomeItemData.listContains;

/**
 * Created by Moing_Admin on 2017/8/2.
 */
public class JsonUtils {
    private static final String KEY_COPYRIGHT = "copyright";
    private static final String KEY_ENDDATE = "enddate";
    private static final String KEY_URL = "url";

    /**
     * 根据json字符串解析为模型数据
     *
     * @param list
     * @param jsonStr
     */
    public static int getJsonData(List list, String jsonStr) {
        int count = 0;//添加成功的数量
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                HomeItemData data = new HomeItemData();
                data.setCopyright(object.getString(KEY_COPYRIGHT));
                data.setEnddate(object.getString(KEY_ENDDATE));
                data.setUrl(object.getString(KEY_URL));
                if (!listContains(list, data)) {
                    list.add(data);
                    count++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return count;
    }
}

package com.as.xiajue.picturebing.view.activity.viewInterface;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.view.custom.DateTranBackTextView;

import java.util.List;

/**
 * xiaJue 2017/9/5创建
 */
public interface IMaxView {
    List<MaxPicItemData> getDataList();

    TextView getMToolbarTitleText();

    Toolbar getMToolbar();

    DateTranBackTextView getDateTextView();

    String getUrl();
}

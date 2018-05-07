package com.cqu.expstuer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.cqu.expstuer.bean.ExpressStu;

import java.util.List;

public class StuAdapter extends CommonAdapter<ExpressStu> {
    
    public StuAdapter(@NonNull Context context, int layoutResId, List<ExpressStu> data) {
        super(context, layoutResId, data);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, ExpressStu item, int position) {

    }
}

package com.cqu.expstuer.ui.address;

import android.content.Intent;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.ContactAddress;
import com.cqu.expstuer.data.SEData;
import com.cqu.expstuer.ui.base.ContListActivity;

import java.util.List;

public class AddressManageActivity extends ContListActivity<ContactAddress> {

    public static final int REQUEST_NEW_ADDRESS = 1;
    List<ContactAddress> mData;
    boolean isMenu = false;
    public static final String ADDRESS = "CONTACT_ADDRESS";

    @Override
    public String getUITitle() {
        return "管理地址";
    }

    @Override
    public void init() {
        mData = SEData.getAddress();
        setRightMenu("管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty()) return;

                isMenu = !isMenu;
                notifyDatasetChanged();

            }
        });
        findViewById(R.id.btn_new_add).setOnClickListener(this);
    }

    @Override
    public CommonAdapter<ContactAddress> getAdapter() {

        return new CommonAdapter<ContactAddress>(this, R.layout.item_contact_address, mData) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final ContactAddress item, int position) {
                helper
                        .setText(R.id.tv_name_phone, item.getContactName() + " " + item.getPhone())
                        .setText(R.id.tv_address, item.getProvince() + item.getCity() + item.getArea() + " " + item.getDetialAddress());

                helper.setVisible(R.id.llMenu, isMenu);

                // 选择地址，点选后返回结果
                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(ADDRESS, item);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                // 删除Item
                helper.setOnClickListener(R.id.ll_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(item);
                        notifyDatasetChanged();
                    }
                });
            }
        };

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_address_manage;
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_new_add:
                myTool.startActivityForResult(NewAddActivity.class, REQUEST_NEW_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_NEW_ADDRESS:
                // 添加上了新地址，刷新列表
                if (resultCode == RESULT_OK) {
                    ContactAddress newAddress = (ContactAddress) data.getSerializableExtra(NewAddActivity.NEW_ADDRESS);
                    if (newAddress != null) {
                        add(newAddress);
                    }
                }
                break;


        }
    }
}

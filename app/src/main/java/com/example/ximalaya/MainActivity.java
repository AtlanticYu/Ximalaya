package com.example.ximalaya;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonRequest mXimalaya = CommonRequest.getInstanse();
        String mAppSecret = "8646d66d6abe2efd14f2891f9fd1c8af";
        mXimalaya.setAppkey("9f9ef8f10bebeaa83e71e62f935bede8");
        mXimalaya.setPackid("com.app.test.android");
        mXimalaya.init(this ,mAppSecret);

        Map<String, String> map = new HashMap<>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(@Nullable CategoryList categoryList) {
                List<Category> categories = categoryList.getCategories();
                if (categories != null) {
                    for (Category category : categories) {
                        Log.d(TAG, "category -- > " + category.getCategoryName());
                        System.out.println("--------分割线1--------");
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "error code is -- > " + i + " error msg -- >" + s);
                System.out.println("--------分割线2--------");
            }
        });
    }
}

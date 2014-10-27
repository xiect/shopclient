package com.brains.framework.activities.base;

import com.brains.app.shopclient.ShoppingApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * 非Map系Activityの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseNormalActivity extends Activity {
    // ここから下はBase系Activity間で共通

    // ----- 一般メンバ -----
    // UI構築用
    protected Activity context;
    protected ShoppingApp app;

    // ----- 画面初期化関連 -----
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        app = (ShoppingApp)getApplicationContext();
//        $ = new CommonActivityUtil<BaseNormalActivity>();
//        $.onActivityCreated( this );
    }
}

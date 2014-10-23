package com.brains.app.shopclient.activities.lib;


import android.database.sqlite.SQLiteDatabase;


import com.brains.app.shopclient.common.AppSettings;
import com.brains.app.shopclient.db.schema.SchemaDefinition;
import com.brains.framework.activities.installation.InstallAppFWBaseActivity;
import com.brains.framework.common.FWUtil;
import com.brains.framework.db.schema.RDBSchema;

/**
 * ユーザがいじる必要のない初期化処理ロジック。
 * APとFWの橋渡しをするため，FW側に置けない部分。
 * @author id:language_and_engineering
 *
 */
public abstract class InstallAppUserBaseActivity extends InstallAppFWBaseActivity
{

    // NOTE:このクラスをActivityではなくしてロジッククラスにしてしまうと，
    // installアクティビティからロジッククラスを必ず呼び出すように規約が生じてしまう。
    // ユーザのコード量を減らしつつ，なおかつFW側に置けないコードなのでこうなる。

    @Override
    protected void injectAppInfoIntoFW()
    {
    	// 应用启动初始化时将设定的情报写入FW
        receiveAppInfoAsFW( new AppSettings(this) );
            // NOTE: AppSettingを参照するので，このコードはFW側に置けない。
            // NOTE: これはパッケージをまたいだ参照渡しの処理だが，継承階層内で行われる。
            // したがって，本Activityは，APの顔とFWの顔という二面性を持つことになる。邪道だが面白い。

    }


    @Override
    protected void init_db_schema()
    {
        // DBとテーブルを作成
        new RDBSchema( this ).createIfNotExists( new SchemaDefinition() );
    }


    @Override
    protected void init_db_data(SQLiteDatabase db)
    {
        FWUtil.d("初期データを投入します。");
        new SchemaDefinition().init_db_data(db, this);
    }

}


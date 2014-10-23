package com.brains.framework.activities.installation;

import com.brains.framework.activities.base.BaseNormalActivity;
import com.brains.framework.common.AbstractAppSettings;
import com.brains.framework.common.FWUtil;
import com.brains.framework.db.DBHelper;
import com.brains.framework.db.dao.FWPrefDAO;
import com.brains.framework.db.dao.IFWDAO;
import com.brains.framework.task.AsyncTasksRunner;
import com.brains.framework.task.SequentialAsyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * アプリ初期化アクティビティの基底クラス
 * @author id:language_and_engineering
 *
 */
public abstract class InstallAppFWBaseActivity extends BaseNormalActivity
{

    /**
     * Android SharedPreferences 封装
     * Framwork meta 数据存储 FWInstallCompletedFlag
     */
    private IFWDAO fwDAO = new FWPrefDAO();


    /**
     * アプリを動かすためのクラス関係などをFW側で初期化。
     * 簡易DIみたいなもの。アプリ側からFW側に情報を注入する。
     */
    protected abstract void injectAppInfoIntoFW();


    /**
     * プリファレンスを初期構築
     */
    protected abstract void init_db_preferences();


    /**
     * 构建DB
     */
    protected abstract void init_db_schema();


    /**
     * DB初始化数据
     */
    protected abstract void init_db_data(SQLiteDatabase db);


    /**
     * 初期化其他
     */
    protected abstract void init_etc();


    /**
     * 应用初期化完成后回调
     */
    protected abstract void onInstallCompleted();


    /**
     * 应用初期化步骤跳过时回调
     */
    protected abstract void onInstallSkipped();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // NOTE:基底クラスのonCreate処理が呼ばれるよりも前に，
        // ロガーのタグ等を初期化しておく必要がある。
        injectAppInfoIntoFW();

        // NOTE: FWの初期化判定に入る前に，必要なら
        // 根据AppSetting 中的配置，决定是否删除 SharedPreferences 中的数据
        clear_prefs_if_required();


        // 根据AppSetting 中的配置，决定是否删除 DB 中的数据
        clear_rdb_if_required();


        super.onCreate(savedInstanceState);
        // TODO: 版本升级处理
        
        // 初期化
        if( requires_installation( this ) )
        {
            // 默认 初始化应用（DB，SharedPreferences 等）
            executeDefaultInstallationProcessAsync();
        }
        else
        {
        	// 跳过应用初始化
            onInstallSkipped();
        }
    }


    /**
     * 必要ならデバッグ用にプリファレンスをクリアする。
     * アプリインストール時のプリファレンス初期化処理を強制的に行わせるため。
     */
    private void clear_prefs_if_required()
    {
        if( FWUtil.mustClearPrefsForDebug() )
        {
            FWUtil.d("プリファレンスのクリアの必要があると判断");
            fwDAO.deleteAll(this);
        }
        else
        {
            FWUtil.d("プリファレンスのクリアの必要なし");
        }
    }


    /**
     * 必要ならデバッグ用にRDBをクリアする。
     * テーブルもデータも削除される。
     */
    private void clear_rdb_if_required()
    {
        if( FWUtil.mustClearPrefsForDebug() )
        {
            FWUtil.d("RDBのクリアの必要があると判断");
            new DBHelper(this).deleteDB();
        }
        else
        {
            FWUtil.d("RDBのクリアの必要なし");
        }
    }


    /**
     * 初期化処理が必要かどうかを判定
     */
    private boolean requires_installation(Context context)
    {
        // DIARY: ここの!の付け忘れで悩んだ。requireとcompletedではフラグの意味が正反対。
        return ! fwDAO.getFWInstallCompletedFlag(context);
    }


    /**
     * デフォルトのアプリ初期化プロセス。
     */
    private void executeDefaultInstallationProcessAsync()
    {
        FWUtil.d("アプリ初期化プロセスを開始");
        final Context context = this;

        new AsyncTasksRunner( new SequentialAsyncTask[]{

            new SequentialAsyncTask(){
                @Override
                protected boolean main() {
                    // preferences 初始化
                    init_db_preferences();

                    // DB初始化
                    init_db_schema();
                    SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
                    init_db_data( db );
                    db.close();

                    // 其他初始化
                    init_etc();

                    // 初始化完成后，更新preferences中的meta数据
                    fwDAO.updateFWInstallCompletedFlag( context, true );
                    FWUtil.d("アプリ初期化プロセスを終了");

                    // 初始化完了后回调容器，通知完成
                    onInstallCompleted();
                    return CONTINUE_TASKS;
                }
            }
        }).begin();
    }


    /**
     * 将AppSettings中的情报，追加到fw的meta 中（preferences）
     */
    protected void receiveAppInfoAsFW( AbstractAppSettings settings )
    {
        FWUtil.receiveAppInfoAsFW( settings );
    }

}
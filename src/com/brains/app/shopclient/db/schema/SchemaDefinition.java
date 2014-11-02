package com.brains.app.shopclient.db.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.brains.app.shopclient.db.entity.Friend;
import com.brains.app.shopclient.db.entity.Message;
import com.brains.app.shopclient.db.entity.Product;
import com.brains.framework.db.schema.AbstractSchemaDefinition;
import com.brains.framework.db.schema.RDBColumn;
import com.brains.framework.db.schema.RDBTable;


/**
 * AP側のテーブル構造と初期データを定義。
 * @author id:language_and_engineering
 *
 */
public class SchemaDefinition extends AbstractSchemaDefinition
{
    // NOTE:
    // ・各テーブルの主キーは「id」。
    // ・SQLiteのカラムで定義可能な型については，下記を参照
    //     http://www.sqlite.org/datatype3.html
    // ・アプリのインストール（初期化）アクティビティから呼び出される。

    @Override
    public void define_tables() {

    	// サンプル用の友達テーブル
    	new RDBTable(this)
    	.nameIs( new Friend().tableName() )
    	.columns(new RDBColumn[]{
    			new RDBColumn().nameIs("id").primaryKey(),
    			new RDBColumn().nameIs("name").comment("名前").typeIs("text").notNull(),
    			new RDBColumn().nameIs("age").comment("年齢").typeIs("integer"),
    			new RDBColumn().nameIs("favorite_flag").comment("お気に入りフラグ").typeIs("integer")
    	})
    	.create();

    	new RDBTable(this)
    	.nameIs( new Message().tableName() )
    	.columns(new RDBColumn[]{
    			new RDBColumn().nameIs("messageid").comment("消息ID").typeIs("text").primaryKey(),
    			new RDBColumn().nameIs("msgtitle").comment("标题").typeIs("text").notNull(),
    			new RDBColumn().nameIs("createtime").comment("创建时间").typeIs("text")
    	})
    	.create();
    	// cart
    	new RDBTable(this)
    	.nameIs( new Product().tableName() )
    	.columns(new RDBColumn[]{
    			new RDBColumn().nameIs("id").primaryKey(),
    			new RDBColumn().nameIs("itemId").comment("itemId").typeIs("text").notNull(),
    			new RDBColumn().nameIs("name").comment("name").typeIs("text").notNull(),
    			new RDBColumn().nameIs("desc").comment("desc").typeIs("text").notNull(),
    			new RDBColumn().nameIs("imgSrc").comment("imgSrc").typeIs("text").notNull(),
    			new RDBColumn().nameIs("price").comment("price").typeIs("text").notNull(),
    			new RDBColumn().nameIs("num").comment("num").typeIs("real").notNull()
    	})
    	.create();
    }


    @Override
    public void init_db_data(SQLiteDatabase db, Context context){
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1001','商品名称A', '商品描述A','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3100.89', 1);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1002','商品名称B', '商品描述B','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3200.89', 2);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1003','商品名称C', '商品描述C','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3300.89', 3);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1004','商品名称D', '商品描述D','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3400.89', 4);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1005','商品名称E', '商品描述E','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3500.89', 5);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1006','商品名称F', '商品描述F','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3600.89', 6);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1007','商品名称G', '商品描述G','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3700.89', 7);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1008','商品名称H', '商品描述F','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3800.89', 8);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1009','商品名称I', '商品描述I','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','3900.89', 9);");
        db.execSQL("insert into t_card (itemId, name, desc,imgSrc,price,num) values ('1010','商品名称J', '商品描述J','http://p.zdmimg.com/201410/19/544323c8a5705.jpg_n4.jpg','4000.89', 10);");
    }

}

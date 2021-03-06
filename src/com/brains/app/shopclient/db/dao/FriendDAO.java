package com.brains.app.shopclient.db.dao;

import java.util.List;

import com.brains.app.shopclient.db.entity.Friend;
import com.brains.framework.db.DBHelper;
import com.brains.framework.db.dao.BaseDAO;

import android.content.Context;

/**
 * 友達を読み書きするクラス。
 * @author id:language_and_engineering
 */
public class FriendDAO extends BaseDAO<Friend>
{

    public FriendDAO(Context context) {
        helper = new DBHelper(context);
    }


    // ------------ C --------------


    /**
     * 1人の友達を保存。
     */
    public Friend create(String name, Integer age, Boolean favoriteFlag)
    {
        // 論理エンティティを構築
        Friend f = new Friend();
        f.setName(name);
        f.setAge( age );
        f.setFavorite_flag( favoriteFlag );

        // DB登録
        f.save(helper);

        return f;
    }


    // ------------ R --------------


    /**
     * 友達を全て新しい順に返す。
     */
    public List<Friend> findAll()
    {
        return findAll(helper, Friend.class);
    }


    /**
     * 特定のIDの友達を１人返す。
     */
    public Friend findById(Long friend_id)
    {
        return findById( helper, Friend.class, friend_id );
    }

        // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
        // findAllやfindByIdの実装を参照。


    // ------------ U --------------


    /**
     * 既存の友達のお気に入り状態を反転させる。
     */
    public Friend invertFavoriteFlag( Long friend_id )
    {
        // idをもとに検索
        Friend f = findById( friend_id );

        // フラグを反転する
        f.setFavorite_flag( ! f.getFavorite_flag() );

        // DB更新
        f.save(helper);

        return f;
    }


    // ------------ D --------------


    /**
     * 特定のIDの友達を削除。
     */
    public void deleteById( Long friend_id )
    {
        Friend f = findById(friend_id);

        // DBからの削除を実行
        f.delete(helper);
    }


}

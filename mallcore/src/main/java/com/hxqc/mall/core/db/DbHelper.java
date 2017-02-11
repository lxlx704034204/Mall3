package com.hxqc.mall.core.db;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

/**
 * DbFlow工具类
 * Created by zhaofan
 */
public class DbHelper {

    /**
     * 删除
     *
     * @param cls
     * @param whereCondition
     */
    public static <T extends Model> void delete(Class<T> cls, Condition... whereCondition) {
        SQLite.delete(cls)
                .where(whereCondition)
                .execute();
    }


    /**
     * 查询单个实体
     *
     * @param cls
     * @param whereCondition
     * @return
     */
    public static <T extends Model> T queryEntity(Class<T> cls, Condition... whereCondition) {

        return SQLite.select()
                .from(cls)
                .where(whereCondition)
                .querySingle();
    }


    /**
     * 同步查询List
     *
     * @param cls
     * @param orderBy        排序
     * @param ascending      是否升序
     * @param whereCondition
     * @return
     */
    public static <T extends Model> List<T> query(Class<T> cls, IProperty orderBy, boolean ascending,
                                                  Condition... whereCondition) {

        return SQLite.select()
                .from(cls)
                .where(whereCondition)
                .orderBy(orderBy, ascending)
                .queryList();
    }

    /**
     * 同步查询List
     *
     * @param cls
     * @param whereCondition
     * @param <T>
     * @return
     */
    public static <T extends Model> List<T> query(Class<T> cls, Condition... whereCondition) {

        return SQLite.select()
                .from(cls)
                .where(whereCondition)
                .queryList();
    }

    /**
     * 同步查询List
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T extends Model> List<T> queryA(Class<T> cls) {

        return SQLite.select()
                .from(cls)
                .queryList();
    }

    /**
     * 异步查询List
     *
     * @param cls
     * @param orderBy             排序
     * @param ascending           是否升序
     * @param whereCondition
     * @param transactionListener
     */
    public static <T extends Model> void queryAsync(Class<T> cls, QueryTransaction.QueryResultListCallback<T> transactionListener,
                                                    IProperty orderBy, boolean ascending, Condition... whereCondition) {
        SQLite.select()
                .from(cls)
                .where(whereCondition)
                .orderBy(orderBy, ascending)
                .async().queryListResultCallback(transactionListener);
    }

    /**
     * 异步查询List
     *
     * @param cls
     * @param whereCondition
     * @param transactionListener
     */
    public static <T extends Model> void queryAsync(Class<T> cls, QueryTransaction.QueryResultListCallback<T> transactionListener,
                                                    Condition... whereCondition) {
        SQLite.select()
                .from(cls)
                .where(whereCondition)
                .async().queryListResultCallback(transactionListener);
    }


    /**
     * 事务保存
     *
     * @param dataBase 数据库
     * @param <T>
     */
    public static <T extends Model> void saveTransaction(Class dataBase, List<T> list) {
        FlowManager.getDatabase(dataBase)
                .executeTransaction(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<T>() {
                            @Override
                            public void processModel(T model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(list).build());
    }

    /**
     * 事务保存 异步
     *
     * @param dataBase 数据库
     * @param <T>
     */
    public static <T extends Model> void saveTransactionAsync(Class dataBase, List<T> list, ProcessModelTransaction.OnModelProcessListener<T> processListener) {
        ProcessModelTransaction<T> processModelTransaction =
                new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<T>() {
                    @Override
                    public void processModel(T model, DatabaseWrapper wrapper) {
                        model.save();
                    }
                }).processListener(processListener).addAll(list).build();
        Transaction transaction = FlowManager.getDatabase(dataBase).beginTransactionAsync(processModelTransaction).build();
        transaction.execute();
    }

    /**
     * 事务保存 FastStoreModel
     *
     * @param dataBase 数据库
     * @param <T>
     */
    public static <T extends Model> void saveTransactionFaster(Class dataBase, List<T> list, Class<T> cls) {
        FastStoreModelTransaction fastStoreModelTransaction = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(cls))
                .addAll(list)
                .build();
        FlowManager.getDatabase(dataBase).executeTransaction(fastStoreModelTransaction);
    }

    /**
     * 事务保存 FastStoreModel 异步
     *
     * @param dataBase 数据库
     * @param <T>
     */
    public static <T extends Model> void saveTransactionFasterAsync(Class dataBase, List<T> list, Class<T> cls, Transaction.Success successCallback) {
        FastStoreModelTransaction fastStoreModelTransaction = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(cls))
                .addAll(list)
                .build();
        Transaction transaction = FlowManager.getDatabase(dataBase).beginTransactionAsync(fastStoreModelTransaction).success(successCallback).build();
        transaction.execute();
    }


}

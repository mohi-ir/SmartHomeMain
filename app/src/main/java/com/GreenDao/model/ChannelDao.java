package com.GreenDao.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.GreenDao.model.Channel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHANNEL".
*/
public class ChannelDao extends AbstractDao<Channel, Long> {

    public static final String TABLENAME = "CHANNEL";

    /**
     * Properties of entity Channel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Address = new Property(1, String.class, "address", false, "ADDRESS");
        public final static Property State = new Property(2, Integer.class, "state", false, "STATE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property FunctionId = new Property(4, Long.class, "functionId", false, "FUNCTION_ID");
        public final static Property WallUnitId = new Property(5, Long.class, "wallUnitId", false, "WALL_UNIT_ID");
    };

    private DaoSession daoSession;

    private Query<Channel> function_ChannelsQuery;
    private Query<Channel> wallUnit_ChannelsQuery;

    public ChannelDao(DaoConfig config) {
        super(config);
    }
    
    public ChannelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHANNEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ADDRESS\" TEXT NOT NULL ," + // 1: address
                "\"STATE\" INTEGER," + // 2: state
                "\"NAME\" TEXT NOT NULL ," + // 3: name
                "\"FUNCTION_ID\" INTEGER," + // 4: functionId
                "\"WALL_UNIT_ID\" INTEGER);"); // 5: wallUnitId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHANNEL\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Channel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getAddress());
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(3, state);
        }
        stmt.bindString(4, entity.getName());
 
        Long functionId = entity.getFunctionId();
        if (functionId != null) {
            stmt.bindLong(5, functionId);
        }
 
        Long wallUnitId = entity.getWallUnitId();
        if (wallUnitId != null) {
            stmt.bindLong(6, wallUnitId);
        }
    }

    @Override
    protected void attachEntity(Channel entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Channel readEntity(Cursor cursor, int offset) {
        Channel entity = new Channel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // address
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // state
            cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // functionId
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // wallUnitId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Channel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAddress(cursor.getString(offset + 1));
        entity.setState(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setFunctionId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setWallUnitId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Channel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Channel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "channels" to-many relationship of Function. */
    public List<Channel> _queryFunction_Channels(Long functionId) {
        synchronized (this) {
            if (function_ChannelsQuery == null) {
                QueryBuilder<Channel> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.FunctionId.eq(null));
                function_ChannelsQuery = queryBuilder.build();
            }
        }
        Query<Channel> query = function_ChannelsQuery.forCurrentThread();
        query.setParameter(0, functionId);
        return query.list();
    }

    /** Internal query to resolve the "channels" to-many relationship of WallUnit. */
    public List<Channel> _queryWallUnit_Channels(Long wallUnitId) {
        synchronized (this) {
            if (wallUnit_ChannelsQuery == null) {
                QueryBuilder<Channel> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.WallUnitId.eq(null));
                wallUnit_ChannelsQuery = queryBuilder.build();
            }
        }
        Query<Channel> query = wallUnit_ChannelsQuery.forCurrentThread();
        query.setParameter(0, wallUnitId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getFunctionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getWallUnitDao().getAllColumns());
            builder.append(" FROM CHANNEL T");
            builder.append(" LEFT JOIN FUNCTION T0 ON T.\"FUNCTION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN WALL_UNIT T1 ON T.\"WALL_UNIT_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Channel loadCurrentDeep(Cursor cursor, boolean lock) {
        Channel entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Function function = loadCurrentOther(daoSession.getFunctionDao(), cursor, offset);
        entity.setFunction(function);
        offset += daoSession.getFunctionDao().getAllColumns().length;

        WallUnit wallUnit = loadCurrentOther(daoSession.getWallUnitDao(), cursor, offset);
        entity.setWallUnit(wallUnit);

        return entity;    
    }

    public Channel loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Channel> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Channel> list = new ArrayList<Channel>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Channel> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Channel> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
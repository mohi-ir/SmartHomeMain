package com.GreenDao.model;

import com.GreenDao.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MEMORY_VALUE".
 */
public class MemoryValue {

    private Long id;
    private int value;
    private Long channelId;
    private Long memoryId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MemoryValueDao myDao;

    private Channel channel;
    private Long channel__resolvedKey;

    private Memory memory;
    private Long memory__resolvedKey;


    public MemoryValue() {
    }

    public MemoryValue(Long id) {
        this.id = id;
    }

    public MemoryValue(Long id, int value, Long channelId, Long memoryId) {
        this.id = id;
        this.value = value;
        this.channelId = channelId;
        this.memoryId = memoryId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMemoryValueDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(Long memoryId) {
        this.memoryId = memoryId;
    }

    /** To-one relationship, resolved on first access. */
    public Channel getChannel() {
        Long __key = this.channelId;
        if (channel__resolvedKey == null || !channel__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChannelDao targetDao = daoSession.getChannelDao();
            Channel channelNew = targetDao.load(__key);
            synchronized (this) {
                channel = channelNew;
            	channel__resolvedKey = __key;
            }
        }
        return channel;
    }

    public void setChannel(Channel channel) {
        synchronized (this) {
            this.channel = channel;
            channelId = channel == null ? null : channel.getId();
            channel__resolvedKey = channelId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Memory getMemory() {
        Long __key = this.memoryId;
        if (memory__resolvedKey == null || !memory__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MemoryDao targetDao = daoSession.getMemoryDao();
            Memory memoryNew = targetDao.load(__key);
            synchronized (this) {
                memory = memoryNew;
            	memory__resolvedKey = __key;
            }
        }
        return memory;
    }

    public void setMemory(Memory memory) {
        synchronized (this) {
            this.memory = memory;
            memoryId = memory == null ? null : memory.getId();
            memory__resolvedKey = memoryId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}

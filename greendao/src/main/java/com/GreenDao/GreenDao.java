package com.GreenDao;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.ToMany;


public class GreenDao {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.GreenDao.model");

        Entity function = schema.addEntity("Function");
        function.addIdProperty();
        function.addStringProperty("name").unique().notNull();
        function.addStringProperty("descriptor").unique().notNull();

        Entity wallUnitType = schema.addEntity("WuType");
        wallUnitType.addIdProperty();
        wallUnitType.addStringProperty("name").unique().notNull();
        wallUnitType.addStringProperty("descriptor").unique().notNull();

        Entity channel = schema.addEntity("Channel");
        channel.addIdProperty();
        channel.addStringProperty("address").notNull();
        channel.addIntProperty("state");
        channel.addStringProperty("name").notNull();

        Entity room = schema.addEntity("Room");
        room.addIdProperty();
        room.addStringProperty("name").unique().notNull();
        room.addStringProperty("descriptor").unique().notNull();

        Entity wallUnit = schema.addEntity("WallUnit");
        wallUnit.addIdProperty();
        wallUnit.addStringProperty("address").unique().notNull();
        wallUnit.addStringProperty("name").notNull();

        Entity memory = schema.addEntity("Memory");
        memory.addIdProperty();
        memory.addStringProperty("name").unique().notNull();

        Entity memoryType = schema.addEntity("MemoryType");
        memoryType.addIdProperty();
        memoryType.addStringProperty("name").unique().notNull();
        memoryType.addStringProperty("descriptor");

        Entity memoryValue = schema.addEntity("MemoryValue");
        memoryValue.addIdProperty();
        memoryValue.addIntProperty("value").notNull();

        // ---------------------- relations
        Property wuTypeId = wallUnit.addLongProperty("wuTypeId").getProperty();
        wallUnit.addToOne(wallUnitType,wuTypeId);
        ToMany wuTypeToWallUnit = wallUnitType.addToMany(wallUnit,wuTypeId);
        wuTypeToWallUnit.setName("wallUnits");

        Property functionId = channel.addLongProperty("functionId").getProperty();
        channel.addToOne(function, functionId);
        ToMany functionToChannel = function.addToMany(channel, functionId);
        functionToChannel.setName("channels");

        Property wallUnitId = channel.addLongProperty("wallUnitId").getProperty();
        channel.addToOne(wallUnit, wallUnitId);
        ToMany wallUnitToChannel = wallUnit.addToMany(channel, wallUnitId);
        wallUnitToChannel.setName("channels");

        Property roomId = wallUnit.addLongProperty("roomId").getProperty();
        wallUnit.addToOne(room, roomId);
        ToMany roomToWallUnit = room.addToMany(wallUnit, roomId);
        roomToWallUnit.setName("wallUnits");

        Property parentId = wallUnit.addLongProperty("parentId").getProperty();
        wallUnit.addToOne(wallUnit, parentId);
        ToMany wallUnitToChildren = wallUnit.addToMany(wallUnit, parentId);
        wallUnitToChildren.setName("childrenWallUnits");

        Property memoryTypeId= memory.addLongProperty("memoryTypeId").getProperty();
        memory.addToOne(memoryType, memoryTypeId);
        ToMany memoryTypeToMemory = memoryType.addToMany(memory, memoryTypeId);
        memoryTypeToMemory.setName("memories");

        Property channelId = memoryValue.addLongProperty("channelId").getProperty();
        memoryValue.addToOne(channel,channelId);
        ToMany channelToMemoryValue = channel.addToMany(memoryValue, channelId);
        channelToMemoryValue.setName("memoryValues");

        Property memoryId = memoryValue.addLongProperty("memoryId").getProperty();
        memoryValue.addToOne(memory,memoryId);
        ToMany memoryToMemoryValue = memory.addToMany(memoryValue, memoryId);
        memoryToMemoryValue.setName("memoryValues");

        // ---------------

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

}

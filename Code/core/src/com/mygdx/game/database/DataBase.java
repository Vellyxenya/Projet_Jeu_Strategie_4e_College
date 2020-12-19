package com.mygdx.game.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class containing all the method/functions/constructors used to create, manipulate, update a database.
 */
public class DataBase {
    int continuous; //line in which datas are being retrieved
    public static Database dbHandler; //used to access database, execute queries etc...

    //private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1; //useless

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS \"Galaxy\"(\"id\" INTEGER NOT NULL PRIMARY KEY,'GalaxyName' VARCHAR(100),'GalaxyX' FLOAT(646456993),'GalaxyY' FLOAT(16));" +
            "CREATE TABLE IF NOT EXISTS \"Systems\"(\"id\" INTEGER NOT NULL PRIMARY KEY,'SystemName' VARCHAR(100),'SystemX' FLOAT(16),'SystemY' FLOAT(16),'Dual' INTEGER(3), 'StarType' FLOAT(16), 'Discovered' BOOLEAN, 'Scanned' BOOLEAN);" +
            "CREATE TABLE IF NOT EXISTS \"Planet\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"PlanetName\" VARCHAR(100),\"HumansNb\" INTEGER,\"Distance\" FLOAT(100),\"PlanetType\" VARCHAR(100),\"Oxygen\" BOOLEAN,\"Water\" BOOLEAN,\"AtmosphereQuantity\" FLOAT(10),\"MagneticField\" FLOAT(100), \"Density\" FLOAT, \"Discovered\" BOOLEAN, \"Scanned\" BOOLEAN, \"SystemId\" INTEGER NOT NULL, CONSTRAINT SYS_FK_141 FOREIGN KEY(\"SystemId\") REFERENCES \"Systems\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Flights\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"Description\" VARCHAR(200),\"Origin\" VARCHAR(50),\"Destination\" VARCHAR(50),\"Fuel\" FLOAT(100));" +
            "CREATE TABLE IF NOT EXISTS \"Material\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"MaterialName\" VARCHAR(100),\"MaterialDescription\" VARCHAR(100),\"Explosivity\" FLOAT(100),\"Resistivity\" FLOAT(100),\"Radioactivity\" FLOAT(100),\"Durability\" FLOAT(100),\"MaterialWeight\" FLOAT(100),\"State\" VARCHAR(100),\"Rarity\" FLOAT(100), \"Unlocked\" Integer(3), \"Quantity\" INTEGER);" +
            "CREATE TABLE IF NOT EXISTS \"Rocket\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"RocketName\" VARCHAR(100),\"RocketDescription\" VARCHAR(200),\"Capacity\" INTEGER(1000000), \"Speed\" INTEGER(10000000), \"Unlocked\" INTEGER(10000000));" +
            "CREATE TABLE IF NOT EXISTS \"Equipment\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"EquipmentName\" VARCHAR(100),\"EquipmentDescription\" VARCHAR(200),\"Weight\" INTEGER(100),\"Radius\" INTEGER(100),\"Unlocked\" INTEGER(2));" +
            "CREATE TABLE IF NOT EXISTS \"Player\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"PlayerName\" VARCHAR(100),\"Level\" FLOAT(100),\"Xp\" INTEGER,\"Time\" INTEGER);" +
            "CREATE TABLE IF NOT EXISTS \"System-Planet\"(\"PlanetId\" INTEGER NOT NULL,\"SystemId\" INTEGER NOT NULL,PRIMARY KEY(\"PlanetId\",\"SystemId\"),CONSTRAINT SYS_FK_85 FOREIGN KEY(\"PlanetId\") REFERENCES \"Planet\"(\"id\"),CONSTRAINT SYS_FK_88 FOREIGN KEY(\"SystemId\") REFERENCES \"System\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Material-Planet\"(\"PlanetId\" INTEGER NOT NULL,\"MaterialId\" INTEGER NOT NULL,\"Quantity\" FLOAT(100),PRIMARY KEY(\"PlanetId\",\"MaterialId\"),CONSTRAINT SYS_FK_96 FOREIGN KEY(\"PlanetId\") REFERENCES \"Planet\"(\"id\"),CONSTRAINT SYS_FK_99 FOREIGN KEY(\"MaterialId\") REFERENCES \"Material\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Material-Rocket\"(\"RocketId\" INTEGER NOT NULL,\"MaterialId\" INTEGER NOT NULL,\"Qantity\" FLOAT(100),PRIMARY KEY(\"RocketId\",\"MaterialId\"),CONSTRAINT SYS_FK_102 FOREIGN KEY(\"RocketId\") REFERENCES \"Rocket\"(\"id\"),CONSTRAINT SYS_FK_105 FOREIGN KEY(\"MaterialId\") REFERENCES \"Material\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Rocket-Equipment\"(\"RocketId\" INTEGER NOT NULL,\"EquipmentId\" INTEGER NOT NULL,PRIMARY KEY(\"RocketId\",\"EquipmentId\"),CONSTRAINT SYS_FK_114 FOREIGN KEY(\"EquipmentId\") REFERENCES \"Equipment\"(\"id\"),CONSTRAINT SYS_FK_117 FOREIGN KEY(\"RocketId\") REFERENCES \"Rocket\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Unlocked\"(\"PlayerId\" INTEGER NOT NULL,\"PlanetId\" INTEGER NOT NULL,\"EquipmentId\" INTEGER NOT NULL,\"MaterialId\" INTEGER NOT NULL,\"RocketId\" INTEGER NOT NULL,PRIMARY KEY(\"PlayerId\",\"PlanetId\",\"EquipmentId\",\"MaterialId\",\"RocketId\"),CONSTRAINT SYS_FK_120 FOREIGN KEY(\"PlayerId\") REFERENCES \"Player\"(\"id\"),CONSTRAINT SYS_FK_123 FOREIGN KEY(\"PlanetId\") REFERENCES \"Planet\"(\"id\"),CONSTRAINT SYS_FK_126 FOREIGN KEY(\"EquipmentId\") REFERENCES \"Equipment\"(\"id\"),CONSTRAINT SYS_FK_129 FOREIGN KEY(\"MaterialId\") REFERENCES \"Material\"(\"id\"),CONSTRAINT SYS_FK_132 FOREIGN KEY(\"RocketId\") REFERENCES \"Rocket\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"Material-Equipment\"(\"MaterialId\" INTEGER NOT NULL,\"EquipmentId\" INTEGER NOT NULL,PRIMARY KEY(\"MaterialId\",\"EquipmentId\"),CONSTRAINT SYS_FK_108 FOREIGN KEY(\"MaterialId\") REFERENCES \"Material\"(\"id\"),CONSTRAINT SYS_FK_111 FOREIGN KEY(\"EquipmentId\") REFERENCES \"Equipment\"(\"id\"));" +
            "CREATE TABLE IF NOT EXISTS \"BuiltRocket\"(\"id\" INTEGER NOT NULL PRIMARY KEY,\"RocketName\" VARCHAR(100),\"PersonalDescription\" VARCHAR(300), \"EquippedModules\" VARCHAR(50)); " +
            "CREATE TABLE IF NOT EXISTS \"BlackHoles\"(\"id\" INTEGER NOT NULL PRIMARY KEY, \"Name\" VARCHAR, \"X\" INTEGER, \"Y\" INTEGER, \"CreationTime\" VARCHAR); " +
            "CREATE TABLE IF NOT EXISTS \"WormHoles\"(\"id\" INTEGER NOT NULL PRIMARY KEY, \"Name\" VARCHAR, \"X1\" INTEGER, \"Y1\" INTEGER, \"X2\" INTEGER, \"Y2\" INTEGER, \"CreationTime\" VARCHAR); ";

    //private static final String GENERAL_DATABASE = "CREATE TABLE IF NOT EXISTS \"Accounts\"(\"PlayerName\" VARCHAR NOT NULL PRIMARY KEY);";

    /**
     * Create or open a database
     * @param create wheter create or open
     * @param dataBaseName name of the database we want to create/open
     */
    public DataBase(boolean create, String dataBaseName) {
        if (create) {
            createNewDB(dataBaseName, DATABASE_CREATE);
            System.out.println("Should add the table");
        }
        else openDataBase(dataBaseName);
    }

    /**
     * Opens the database containing the accounts
     * @param dataBaseName
     */
    public DataBase(String dataBaseName) {
        openDataBase(dataBaseName);
    }

    /**
     * Get multiple datas from a database
     * @param query
     * @return the datas that have been retrieved, according the the @query
     */
    public ArrayList<String> readDatas(String query) {
        ArrayList<String> result = new ArrayList<String>();
        String sentence;
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            continuous = 1;
            sentence = "";
            sentence += String.valueOf(cursor.getString(0));
            while (continuous < 35) {
                try {
                    String value = String.valueOf(cursor.getString(continuous));
                    sentence += " ";
                    sentence += value;
                    continuous++;
                }catch(NoSuchMethodError error){
                    break;
                }
            }
            result.add(sentence);
        }
        return result;
    }

    /**
     * Retrieve some systems
     * @param query SQL query
     * @return the retrieved systems
     */
    public ArrayList<String> getSystems(String query) {
        ArrayList<String> result = new ArrayList<String>();
        String sentence;
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            sentence = "";
            sentence += String.valueOf(cursor.getString(0));
            sentence += " ";
            sentence += String.valueOf(cursor.getString(1));
            sentence += " ";
            sentence += String.valueOf(cursor.getString(2));
            result.add(sentence);
        }
        return result;
    }

    /**
     * Get one specific system
     * @param query
     * @return
     */
    public int getSystem(String query) {
        int result = -1;
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            result = Integer.parseInt(cursor.getString(0));
        } catch (NoSuchMethodError e){
            result = -1;
        }
        return result;
    }

    /**
     * Get a list of planets in a system
     * @param query
     * @return
     */
    public ArrayList getPlanets(String query) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()){
            ints.add(Integer.parseInt(cursor.getString(0)));
        }
        return ints;
    }

    /**
     * Get a unique data with a given query
     * @param query
     * @return a String corresponding to the wanted data
     */
    public String getOneData(String query) {
        String data = "";
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            data = cursor.getString(0);
            if(data == null) data = "0";
        } catch (NoSuchMethodError e){
            data = "999999";
        }
        return data;
    }

    /**
     * used to retrieve either a system or a planet, depending on the name of the current position.
     * @param currentOrigin name of the system in which the player is
     * @return a name of a planet or system
     */
    public String getPlaOrSys(String currentOrigin) {
        String data = "";
        DatabaseCursor cursor;
        try {
            cursor = dbHandler.rawQuery("SELECT SystemId FROM Planet WHERE PlanetName = '"+currentOrigin+"' ;");
            data = cursor.getString(0);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        } catch (NoSuchMethodError e){
            try {
                cursor = dbHandler.rawQuery("SELECT id FROM Systems WHERE SystemName = '"+currentOrigin+"' ;");
                data = cursor.getString(0);
            } catch (SQLiteGdxException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodError e1){
                e1.printStackTrace();
                System.out.println("This should never happen : 2 fails in a row");
            }
        }
        return data;
    }

    /**
     * Create a new database
     * @param dataBaseName name of the database
     * @param statement sql statements required to construct the database
     */
    public void createNewDB(String dataBaseName, String statement) {
        Gdx.app.log("DataBase", "creation started");
        dbHandler = DatabaseFactory.getNewDatabase(dataBaseName,
                DATABASE_VERSION, statement, null);
        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(statement);
            System.out.println("DATABASE_CREATE");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        Gdx.app.log("DataBase", "created successfully");
    }

    /**
     * Add a new player in the Accounts database
     * @param playerName
     * @return
     */
    public boolean addNewPlayer(String playerName){
        try {
            //dbHandler.execSQL("DELETE FROM Accounts");
            dbHandler.execSQL("INSERT INTO Accounts('PlayerName') VALUES('"+playerName+"');");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
            System.out.println("FAILED TO INSERT NEW PLAYER");
            return false;
        }
        return true;
    }

    /**
     * Open the account database
     * @param dataBaseName
     */
    public void openDataBase(String dataBaseName){
        dbHandler = DatabaseFactory.getNewDatabase(/*"Accounts/accounts.db"*/dataBaseName,
                DATABASE_VERSION, null, null);
        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        Gdx.app.log("DataBase", "opened successfully");
    }

    /**
     * Update an opened database with a certain query
     * @param query
     * @throws SQLException may be useless
     */
    public void updateDB(String query) throws SQLException {
        try {
            dbHandler.execSQL(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        //Gdx.app.log("DataBase", "updated successfully");
    }

    /**
     * Another version of #updateDB, but without throwing an SQLException
     * @param query
     */
    public void updateDataBase(String query) {
        try {
            dbHandler.execSQL(query);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the currently opoeened database
     */
    public void closeDB() {
        try {
            dbHandler.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        dbHandler = null;
        Gdx.app.log("DataBase", "dispose");
    }
}
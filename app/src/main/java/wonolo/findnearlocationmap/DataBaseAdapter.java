package wonolo.findnearlocationmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_USERNAME = "username";
    static final String KEY_PASSWORD = "password";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDataBase";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, "
            + "username text not null, password text not null);";
    private Context mContext;
    private DatabaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    public DataBaseAdapter(Context context){
        this.mContext = context;
        mDataBaseHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(DATABASE_CREATE);
            } catch (SQLiteException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e(TAG,"Upgrading database from version "+oldVersion+" to "+newVersion+", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    public DataBaseAdapter open() throws SQLiteException{
        mDatabase = mDataBaseHelper.getWritableDatabase();
        return  this;
    }
    public void close(){
        mDataBaseHelper.close();
    }
    //---insert a contact into the database---
    public long insertContact(String name, String pass){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME,name);
        initialValues.put(KEY_PASSWORD,pass);
        return mDatabase.insert(DATABASE_TABLE,null,initialValues);
    }
    //---deletes a particular contact---
    public boolean deleteContact(long rowId){
        return mDatabase.delete(DATABASE_TABLE,KEY_ROWID + "="+rowId,null) > 0;
    }
    //---retrieves all the contacts---
    public Cursor getAllContacts(){
        return mDatabase.query(DATABASE_TABLE,new String[]{
                KEY_ROWID, KEY_USERNAME, KEY_PASSWORD},null,null,null,null,null);
    }
    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException {
        Cursor mCursor = mDatabase.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_USERNAME, KEY_PASSWORD}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor!=null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //---updates a contact---
    public boolean updateContact(long rowId, String name, String pass){
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME,name);
        args.put(KEY_PASSWORD,pass);
        return mDatabase.update(DATABASE_TABLE,args,KEY_ROWID+"="+rowId,null)>0;
    }
}

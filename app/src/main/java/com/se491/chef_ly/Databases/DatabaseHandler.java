package com.se491.chef_ly.Databases;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.ShoppingListItem;

import java.util.ArrayList;

//http://stackoverflow.com/questions/26375184/inserting-and-deleting-values-in-with-my-dbhelper-class
public class DatabaseHandler extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHandler";
    private static final String DB_FILE_NAME = "chefly.db";
    private static final int DB_VERSION = 4;

    public DatabaseHandler(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeTable.SQL_CREATE);
        db.execSQL(RecipeDetailTable.SQL_CREATE);
        db.execSQL( ShoppingList.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RecipeTable.SQL_DELETE);
        db.execSQL(RecipeDetailTable.SQL_DELETE);
        db.execSQL(ShoppingList.SQL_DELETE);
        onCreate(db);
    }
    public Recipe createRecipeItem(Recipe item) {
        //ContentValues values = item.getRecipes();
        // db.insert(RecipeTable.RECIPE_TABLE_ITEMS, null, values);
        return item;
    }

    public long getRecipeItemsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long l =  DatabaseUtils.queryNumEntries(db, RecipeTable.RECIPE_TABLE_ITEMS);
        db.close();
        return l;
    }

    public ArrayList<ShoppingListItem> getShoppingList(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ShoppingListItem> listItems = new ArrayList<>();

        Cursor cursor = db.query(ShoppingList.TABLE_LIST_ITEMS, ShoppingList.ALL_COLUMNS , null, null, null, null, ShoppingList.COLUMN_NAME);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ShoppingList.COLUMN_LIST_ID));
                String name = cursor.getString(cursor.getColumnIndex(ShoppingList.COLUMN_NAME));
                int qty = cursor.getInt(cursor.getColumnIndex(ShoppingList.COLUMN_QUANTITY));
                String uom = cursor.getString(cursor.getColumnIndex(ShoppingList.COLUMN_UNIT));
                boolean purchased = cursor.getInt(cursor.getColumnIndex(ShoppingList.COLUMN_PURCHASED)) > 0;

                listItems.add(new ShoppingListItem(id, name, qty,uom,purchased));
            } while (cursor.moveToNext());
            return listItems;
        }

        cursor.close();
        db.close();
        return listItems;
    }

    public void addItemToShoppingList(Ingredient i, boolean purchased){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ShoppingList.COLUMN_NAME, i.getItem().getName());
        values.put(ShoppingList.COLUMN_QUANTITY, i.getQty());
        values.put(ShoppingList.COLUMN_UNIT, i.getItem().getUom());
        values.put(ShoppingList.COLUMN_PURCHASED, purchased);

        long result = db.insert(ShoppingList.TABLE_LIST_ITEMS, null, values);

        db.close();
    }
    public void updateShoppingListItem(ShoppingListItem i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingList.COLUMN_NAME, i.getName());
        values.put(ShoppingList.COLUMN_QUANTITY, i.getQty());
        values.put(ShoppingList.COLUMN_UNIT, i.getUnitOfMeasure());
        values.put(ShoppingList.COLUMN_PURCHASED, i.isPurchased());
        db.update(ShoppingList.TABLE_LIST_ITEMS, values, ShoppingList.COLUMN_LIST_ID + " = " + i.getId(), null);
        db.close();
    }

    public void deleteItemFromShoppingList(ArrayList<ShoppingListItem> items){
        SQLiteDatabase db = this.getWritableDatabase();
        for(ShoppingListItem item : items){
            long result = db.delete(ShoppingList.TABLE_LIST_ITEMS, ShoppingList.COLUMN_LIST_ID + " = " + item.getId(), null);
            Log.d(TAG, "id -> " +item.getId() + " result ->" + result);
        }

        db.close();
    }
}
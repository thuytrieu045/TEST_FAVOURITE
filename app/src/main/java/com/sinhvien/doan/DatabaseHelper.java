package com.sinhvien.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEN_DATABASE = "BakingRecipeApp.db";
    private static final int DATABASE_VERSION = 2; // Tăng version để kích hoạt onUpgrade

    // Bảng Users
    public static final String BANG_USERS = "users";
    public static final String COT_USER_ID = "user_id";
    public static final String COT_FIREBASE_UID = "firebase_uid";

    //Bang Product
    private static final String BANG_PRODUCT = "products";
    private static final String COT_PRODUCT_ID = "id";
    private static final String COT_PRODUCT_NAME = "name";
    private static final String COT_PRODUCT_DESCRIPTION = "description";
    private static final String COT_PRODUCT_AVATAR = "avatar";

    // Bảng Recipes
    public static final String BANG_RECIPES = "recipes";
    public static final String COT_RECIPE_ID = "recipe_id";
    public static final String COT_TEN_RECIPE = "recipe_name";
    public static final String COT_INGREDIENTS = "ingredients";
    public static final String COT_STEPS = "steps";
    public static final String COT_IMG_URL = "img_src";
    public static final String COT_CATEGORY = "cate";
    public static final String COT_TIME = "time";
    public static final String COT_DOKHO = "difficulty";

    // Chuỗi tạo bảng Users với các cột thanh toán
    private static final String CREATE_BANG_USERS = "CREATE TABLE " + BANG_USERS + " (" +
            COT_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COT_FIREBASE_UID + " TEXT UNIQUE NOT NULL, " +
            "momo_number TEXT, " +
            "zalopay_number TEXT, " +
            "vietcombank_account TEXT, " +
            "mbbank_account TEXT, " +
            "vietinbank_account TEXT)";

    private static final String CREATE_BANG_RECIPES = "CREATE TABLE " + BANG_RECIPES + " (" +
            COT_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COT_TEN_RECIPE + " TEXT NOT NULL, " +
            COT_INGREDIENTS + " TEXT NOT NULL, " +
            COT_STEPS + " TEXT NOT NULL, " +
            COT_USER_ID + " INTEGER NOT NULL, " +
            COT_IMG_URL + " TEXT, " +
            COT_CATEGORY + " INTEGER NOT NULL, " +
            COT_TIME + " INTEGER NOT NULL, " +
            COT_DOKHO + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + COT_USER_ID + ") REFERENCES " + BANG_USERS + "(" + COT_USER_ID + "))";
    //Tao bang Product

    private static final String CREATE_BANG_PRODUCT = "CREATE TABLE " + BANG_PRODUCT + " (" +
            COT_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COT_PRODUCT_NAME + " TEXT, " +
            COT_PRODUCT_DESCRIPTION + " TEXT, " +
            COT_PRODUCT_AVATAR + " TEXT) ";

    public DatabaseHelper(Context context) {
        super(context, TEN_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANG_USERS);
        db.execSQL(CREATE_BANG_RECIPES);
        db.execSQL(CREATE_BANG_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Thêm các cột mới vào bảng users
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN momo_number TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN zalopay_number TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN vietcombank_account TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN mbbank_account TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN vietinbank_account TEXT");
        }
        db.execSQL("DROP TABLE IF EXISTS " + BANG_PRODUCT);
        onCreate(db);
    }

    public int getUserId(String firebaseUid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userId = -1;

        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE firebase_uid = ?", new String[]{firebaseUid});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        } else {
            ContentValues values = new ContentValues();
            values.put("firebase_uid", firebaseUid);
            long newUserId = db.insert("users", null, values);
            if (newUserId != -1) {
                userId = (int) newUserId;
            }
        }
        cursor.close();
        return userId;
    }

    public void updatePaymentInfo(int userId, String momo, String zalopay, String vietcombank, String mbbank, String vietinbank) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("momo_number", momo);
        values.put("zalopay_number", zalopay);
        values.put("vietcombank_account", vietcombank);
        values.put("mbbank_account", mbbank);
        values.put("vietinbank_account", vietinbank);
        db.update(BANG_USERS, values, COT_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }
    public List<Product> getFavoriteProductsByUser(int userId) {
        List<Product> favoriteProducts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.product_id, p.name, p.description, p.avatar, p.image " +
                "FROM product p " +
                "JOIN favorites f ON p.product_id = f.product_id " +
                "WHERE f.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));


                    Product product = new Product(id, name, description, avatar, image);
                    favoriteProducts.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return favoriteProducts;
    }
    public void addToFavorites(int userId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("product_id", productId);

        long result = db.insert("favorites", null, values);
        db.close();

        if (result == -1) {
            Log.e("DBHelper", "Lỗi khi thêm vào danh sách yêu thích!");
        } else {
            Log.d("DBHelper", "Đã thêm công thức " + productId + " vào danh sách yêu thích của user " + userId);
        }
    }

    public void removeFromFavorites(int userId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("favorites", "user_id = ? AND product_id = ?", new String[]{String.valueOf(userId), String.valueOf(productId)});
        db.close();

        if (deletedRows > 0) {
            Log.d("DBHelper", "Đã xóa công thức " + productId + " khỏi danh sách yêu thích của user " + userId);
        } else {
            Log.e("DBHelper", "Không tìm thấy bài hát trong danh sách yêu thích để xóa!");
        }
    }

    public boolean isFavorite(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND product_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(productId)});

        boolean isFav = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isFav = cursor.getInt(0) > 0; // Nếu COUNT(*) > 0, bài hát là yêu thích
            }
            cursor.close();
        }
        db.close();
        return isFav;
    }

}
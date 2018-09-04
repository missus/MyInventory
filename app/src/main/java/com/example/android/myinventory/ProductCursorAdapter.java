/*
 * Created by Karolin Fornet.
 * Copyright (c) 2017.  All rights reserved.
 */

package com.example.android.myinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myinventory.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.name);
        TextView brandView = view.findViewById(R.id.brand);
        TextView priceView = view.findViewById(R.id.price);
        TextView quantityView = view.findViewById(R.id.quantity);
        ImageView buyNowButton = view.findViewById(R.id.buy_now);
        String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        String brand = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_BRAND));
        int price = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        final int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        nameView.setText(name);
        brandView.setText(brand);
        priceView.setText(String.valueOf(price) + "$");
        quantityView.setText(String.valueOf(quantity));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    Log.v("CatalogActivity", "Uri: " + currentProductUri);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);
                    int rowsEffected = context.getContentResolver().update(currentProductUri, values, null, null);
                } else {
                    Toast.makeText(context, R.string.catalog_no_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
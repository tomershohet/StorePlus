package com.device.storeplus.storeplus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.device.storeplus.storeplus.models.Bag;
import com.device.storeplus.storeplus.models.SingleItem;

import java.util.ArrayList;


public class BagActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        this.setScreenSettings();
        onNewNfcConnected(1);
    }

    @Override
    public void onResume() {
        this.setScreenSettings();
        super.onResume();  // Always call the superclass method first
    }

    private void setScreenSettings() {
        View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        decorView.setSystemUiVisibility(uiOptions);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void onNewNfcConnected(int uid) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);

        Bag bag = ServerApi.getUserBag(1);
        ServerApi.getItemDetails(0, "", "");
        ServerApi.getUserDetails(uid);
        try {
            ServerApi.addItemToBag(0, 0, "");
            ServerApi.removeItemFromBag(0, 0, "");
            ServerApi.cleanUserBag(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 2; i++) {
            View singleBagItemView = this.createSingleBagItemView(bag.items.get(i));
            layout.addView(singleBagItemView);
        }
    }

    private View createSingleBagItemView(SingleItem Item) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_bag_item = inflater.inflate(R.layout.single_bag_item, null);

        ((TextView)single_bag_item.findViewById(R.id.bag_cloth_price)).setText(String.valueOf(Item.price) + Item.currency);
        ((TextView)single_bag_item.findViewById(R.id.bag_cloth_name)).setText(Item.title);
        ((ImageView)single_bag_item.findViewById(R.id.bag_cloth_image)).setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.shirt));

        return single_bag_item;
    }

    public class RetriveBag extends AsyncTask<Integer, Void, Bitmap> {

        private Exception exception;

        protected Bitmap doInBackground(Integer... uids) {
            Bitmap bmp = null;
            try {
                Integer uid = uids[0];
                ServerApi.getUserBag(uid);

            }
            catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            }

            return bmp;
        }

        protected void onPostExecute(Bitmap itemBitmap) {
            if (this.exception != null)
                return;

            // Init the bag with items
            //ImageView imgItem = (ImageView)findViewById(R.id.clotheImageView);
            //imgItem.setImageBitmap(itemBitmap);
        }
    }
}

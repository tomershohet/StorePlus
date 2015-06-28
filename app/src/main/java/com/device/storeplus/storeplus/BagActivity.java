package com.device.storeplus.storeplus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.device.storeplus.storeplus.models.Bag;
import com.device.storeplus.storeplus.models.SingleItem;
import com.device.storeplus.storeplus.util.SwipeDismissTouchListener;

import org.w3c.dom.Text;

import java.net.URL;
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
        final LinearLayout bagLayout = (LinearLayout) findViewById(R.id.bag_items_linear);

        Bag bag = ServerApi.getUserBag(1);

        // Create users items
        for (int i = 0; i < bag.items.size(); i++) {
            final View singleBagItemView = this.createSingleBagItemView(bag.items.get(i));

            // Create a generic swipe-to-dismiss touch listener.
            singleBagItemView.setOnTouchListener(new SwipeDismissTouchListener(
                    singleBagItemView,
                    null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            bagLayout.removeView(singleBagItemView);
                        }
                    }));

            bagLayout.addView(singleBagItemView);
        }

        // Set user pictures and name
        new RetriveUserProfileImage().execute(new String[]{bag.user.imageUrl});
        ((TextView)findViewById(R.id.bag_user_title)).setText(bag.user.name);

        // Set the bag details
        ((TextView)findViewById(R.id.bag_details)).setText(this.getString(R.string.items_in_bag) + ":" + bag.items.size());

        /*ServerApi.getItemDetails(0, "", "");
        ServerApi.getUserDetails(uid);
        try {
            ServerApi.addItemToBag(0, 0, "");
            ServerApi.removeItemFromBag(0, 0, "");
            ServerApi.cleanUserBag(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static Integer itemsCounter = 0;
    private View createSingleBagItemView(SingleItem Item) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_bag_item = inflater.inflate(R.layout.single_bag_item, null);

        ((TextView)single_bag_item.findViewById(R.id.bag_cloth_price)).setText(String.valueOf(Item.price) + Item.currency);
        ((TextView)single_bag_item.findViewById(R.id.bag_cloth_name)).setText(Item.title);

        single_bag_item.setId(itemsCounter++);
        new RetriveItemImage().execute(new String[]{String.valueOf(single_bag_item.getId()), Item.imageUrl});

        single_bag_item.setClickable(true);
        return single_bag_item;
    }

    public class RetriveUserProfileImage extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;

        protected Bitmap doInBackground(String... params) {
            Bitmap retValue = null;

            try {
                URL imageUrl = new URL(params[0]);
                retValue = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }
            catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            }

            return retValue;
        }

        protected void onPostExecute(Bitmap resultBitmap) {
            if (this.exception != null)
                return;

            // Init the bag with items
            ImageView imgItem = (ImageView)findViewById(R.id.bag_user_imageView);
            imgItem.setImageBitmap(resultBitmap);
        }
    }

    public class RetriveItemImage extends AsyncTask<String, Void, Pair<Integer, Bitmap>> {

        private Exception exception;

        protected Pair<Integer, Bitmap> doInBackground(String... params) {
            Pair<Integer, Bitmap> retValue = null;

            try {
                Integer imageViewID = Integer.valueOf(params[0]);
                URL imageUrl = new URL(params[1]);
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                retValue = new Pair<>(imageViewID, bmp);
            }
            catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            }

            return retValue;
        }

        protected void onPostExecute(Pair<Integer, Bitmap> resultBitmap) {
            if (this.exception != null)
                return;

            // Init the bag with items
            View bagViewItem = (View)findViewById(resultBitmap.first);
            ImageView imgItem = (ImageView)bagViewItem.findViewById(R.id.bag_cloth_image);
            imgItem.setImageBitmap(resultBitmap.second);
        }
    }
}

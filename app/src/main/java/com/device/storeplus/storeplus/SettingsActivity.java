package com.device.storeplus.storeplus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.device.storeplus.storeplus.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.Map;

public class SettingsActivity extends Activity {
    public static final String PREF_SHARED_ITEM = "device_settings";
    public static final String PREF_ITEM = "currItem_string";
    public static final String PREF_DEVICE_TYPE = "device_type";
    public static final String DEVICE_KIOSK = "Kiosk";
    public static final String DEVICE_HANGER = "Hanger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button scanButton = (Button)findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(SettingsActivity.this);
                integrator.addExtra("SCAN_WIDTH", 640);
                integrator.addExtra("SCAN_HEIGHT", 480);
                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                //customize the prompt message before scanning
                integrator.addExtra("PROMPT_MESSAGE", R.string.scanner_start);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        });

        RadioGroup deviceTypeGroup = (RadioGroup)findViewById(R.id.deviceTypeGroup);
        // Set the deviceType on the last selected one
        // Save the curr item on shared preferences
        String deviceType = getSharedPreferences(PREF_SHARED_ITEM, MODE_PRIVATE).getString(PREF_DEVICE_TYPE, "");
        switch (deviceType) {
            case DEVICE_KIOSK:
                deviceTypeGroup.check(R.id.radioButtonKiosk);
                break;
            case DEVICE_HANGER:
            default:
                deviceTypeGroup.check(R.id.radioButtonHanger);
        }

        deviceTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {
                // Save the curr item on shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(PREF_SHARED_ITEM, MODE_PRIVATE).edit();
                switch (checkedId) {
                    case R.id.radioButtonKiosk:
                        editor.putString(PREF_DEVICE_TYPE, DEVICE_KIOSK);
                        break;
                    case R.id.radioButtonHanger:
                    default:
                        editor.putString(PREF_DEVICE_TYPE, DEVICE_HANGER);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                Toast.makeText(getBaseContext(), R.string.result_succeeded + ": " + result.toString(), Toast.LENGTH_LONG).show();

                String jsCurrItem = serverApi.getItemDetails(1, "", "");

                // Save the curr item on shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(PREF_SHARED_ITEM, MODE_PRIVATE).edit();
                editor.putString(PREF_ITEM, jsCurrItem);
                editor.commit();

                //showDialog(R.string.result_succeeded, result.toString());
            } else {
                Toast.makeText(getBaseContext(),R.string.result_failed , Toast.LENGTH_LONG).show();

                //showDialog(R.string.result_failed, getString(R.string.result_failed_why));
            }
        }
    }

}

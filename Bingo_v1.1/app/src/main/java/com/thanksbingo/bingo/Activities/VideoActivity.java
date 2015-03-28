package com.thanksbingo.bingo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.VideoView;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.Entities.DoorNo;
import com.thanksbingo.bingo.R;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.FoodInfoContract;
import com.thanksbingo.icondownloader.IconDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VideoActivity extends ActionBarActivity {

    Context context = this;

    TextView loading_text;
    int allll = 0;
    final String LOADING_MSG = "필요한 리소스를 로딩중입니다... ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        final String json_data = intent.getStringExtra(IntroActivity.JSON_DATA);
        final String msg = intent.getStringExtra(IntroActivity.MSG);

//        Log.i(CONST_STRINGS.BINGO_LOG, json_data);

        loading_text = (TextView)findViewById(R.id.loading_text);

        VideoView vv = (VideoView)this.findViewById(R.id.introVideo);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.fish_animation;
        vv.setVideoURI(Uri.parse(uri));

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (msg.compareTo(IntroActivity.INITIAL_UPDATE) == 0) {
                    new NewFoodDataProcess().execute(json_data);
                }
                else if (msg.compareTo(IntroActivity.NEED_UPDATE) == 0) {
                    new UpdateFoodDataProcess().execute(json_data);
                }
                else {
                    finish();
                }
            }
        });
        vv.start();


    }

    private class UpdateFoodDataProcess extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String msg = LOADING_MSG + "(" + values[0].intValue() + "/" + allll + ")";

            loading_text.setText(msg);
        }

        @Override
        protected Integer doInBackground(String... params) {

            if (params.length < 1)
                return null;
            String raw_json = params[0];

            try {
                JSONObject jObject = new JSONObject(raw_json);

                boolean need_update = jObject.getBoolean("need_update");
                if (need_update) {
                    JSONArray jArray = new JSONArray(jObject.getString("food_info_list"));

                    int last_history = jObject.getInt("last_history");

                    BingoDB bingoDB = new BingoDB(context);
                    IconDownloader icon_downloader = new IconDownloader(null, CONST_STRINGS.ICON_PATH);

                    JSONArray type0_arr = new JSONArray(jObject.getString("type0"));
                    if (type0_arr.length() != 0) {
                        // type0 : 새로이 추가된 데이터
                        allll = jArray.length();
                        for (int i = 0; i < jArray.length(); i++) {
                            handleFoodInfoJSONObject(bingoDB, icon_downloader, jArray.getJSONObject(i));
                            publishProgress(i);
                        }
                    }
                    JSONArray type1_arr = new JSONArray(jObject.getString("type1"));
                    if (type1_arr.length() != 0) {
                                /* type 1: modified data (except icon image)
                                 * 수정된 데이터 값 업데이트.
                                 * 아직 구현 안함.
                                 */
                    }
                    JSONArray type2_arr = new JSONArray(jObject.getString("type2"));
                    if (type2_arr.length() != 0) {
                                /* type 2: modified data & icon
                                 * 수정된 데이터 값 업데이트와 아이콘 다운
                                 * 아직 구현 안함
                                 */
                    }

                    return last_history;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer last_history) {
            super.onPostExecute(last_history);

            if (last_history == null)
                return;
            else {
                SharedPreferences sharedPref = context.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, last_history.intValue());
                editor.commit();
                callNextActivity();
                ((VideoActivity)context).finish();
            }
        }
    }

    private class NewFoodDataProcess extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String msg = LOADING_MSG + "(" + values[0].intValue() + "/" + allll + ")";
            loading_text.setText(msg);
        }

        @Override
        protected Integer doInBackground(String... params) {

            if (params.length < 1)
                return null;
            String raw_json = params[0];
            try {
                JSONObject jObject = new JSONObject(raw_json);

                boolean need_update = jObject.getBoolean("need_update");
                if (need_update) {
                    JSONArray jArray = new JSONArray(jObject.getString("food_info_list"));

                    int last_history = jObject.getInt("last_history");

                    BingoDB bingoDB = new BingoDB(context);
                    IconDownloader icon_downloader = new IconDownloader(null, CONST_STRINGS.ICON_PATH);

                    allll = jArray.length();
                    for (int i = 0; i < jArray.length(); i++) {
                        handleFoodInfoJSONObject(bingoDB, icon_downloader, jArray.getJSONObject(i));
                        publishProgress(i);
                    }

                    return last_history;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer last_history) {
            super.onPostExecute(last_history);

            if (last_history == null)
                return;
            else {
                SharedPreferences sharedPref = context.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, last_history.intValue());
                editor.commit();
                callNextActivity();
                ((VideoActivity)context).finish();
            }
        }


    }

    private void handleFoodInfoJSONObject(BingoDB bingoDB, IconDownloader icon_downloader, JSONObject food_info) {

        try {
            FoodInfoContract.FIData data = new FoodInfoContract.FIData();
            data.food_id = food_info.getInt("food_id");
            data.food_name = food_info.getString("food_name");
            data.rec_exp = food_info.getInt("rec_exp");
            data.frequency = food_info.getInt("frequency");
            try {
                data.icon_img1 = icon_downloader.downloadIconImage(food_info.getString("icon1"));
                data.icon_img2 = icon_downloader.downloadIconImage(food_info.getString("icon2"));
            } catch (IconDownloader.NoIconUrlException e) {
                e.printStackTrace();
            } catch (IconDownloader.UndecidedIconsDirPath e) {
                e.printStackTrace();
            }
            bingoDB.writeFoodInfoToDB(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callNextActivity() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        if (DoorNo.checkYo(sharedPref)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, FridgeCustom.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

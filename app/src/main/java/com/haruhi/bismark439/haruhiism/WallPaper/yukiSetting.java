package com.haruhi.bismark439.haruhiism.WallPaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.haruhi.bismark439.haruhiism.R;

import static com.haruhi.bismark439.haruhiism.MainActivity.PREF_NAME;
import static com.haruhi.bismark439.haruhiism.MainActivity.sharedEditor;
import static com.haruhi.bismark439.haruhiism.MainActivity.sharedPref;

public class yukiSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuki_setting);
    }

    public void onClick(View view) {
        String tag = (String) view.getTag();
saveLWP(getApplicationContext(),tag);

       /* Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, yukiWall.class));
        startActivity(intent);*/
        finish();
    }

    public static String loadLWP(Context context) {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            sharedEditor = sharedPref.edit();
        }
                String name = sharedPref.getString("haruhiismLWP", "SUZUMIYA HARUHI");
            return name;
    }

    public static void saveLWP(Context context,String name) {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            sharedEditor = sharedPref.edit();
        }
        sharedEditor.putString("haruhiismLWP", name);
        sharedEditor.commit();
    }
}

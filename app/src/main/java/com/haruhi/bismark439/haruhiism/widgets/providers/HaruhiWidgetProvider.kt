package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_HARUHI
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.SRC_WIDGET
import java.io.IOException

/**
 * Created by Bismark439 on 12/01/2018.
 */
class HaruhiWidgetProvider : IWidgetProvider(
    WidgetCreater.getCalendar(2020, 10, 25),
    R.string.latest_title,
    R.drawable.haruhi1,
    "#eeed7e10",
    FOLDER_HARUHI,
    HaruhiWidgetProvider::class.java
)
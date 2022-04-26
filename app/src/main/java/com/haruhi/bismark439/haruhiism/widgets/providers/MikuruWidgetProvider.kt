package com.haruhi.bismark439.haruhiism.widgets.providers

import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.Constants

/**
 * Created by Bismark439 on 19/01/2018.
 */
class MikuruWidgetProvider : IWidgetProvider(
    WidgetCreater.getCalendar(2003, 5, 6),
    R.string.melancholy,
    R.drawable.mikuruw,
    "#EEc80000",
    Constants.FOLDER_MIKURU,
    MikuruWidgetProvider::class.java
)



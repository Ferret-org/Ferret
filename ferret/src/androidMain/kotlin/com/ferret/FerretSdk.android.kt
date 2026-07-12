package com.ferret

import android.content.Context
import com.ferret.di.FerretKoin

fun FerretSdk.init(
    context: Context,
    configuration: FerretConfiguration = FerretConfiguration()
) {
    FerretKoin.start(context.applicationContext, configuration)
}

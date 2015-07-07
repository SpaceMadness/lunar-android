package com.spacemadness.lunar;

import android.content.Context;

/**
 * Created by alementuev on 7/6/15.
 */
public interface RuntimePlatformFactory
{
    RuntimePlatform createRuntimePlatform(Context context);
}

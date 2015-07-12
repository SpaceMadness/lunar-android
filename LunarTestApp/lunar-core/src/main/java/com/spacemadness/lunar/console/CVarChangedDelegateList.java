package com.spacemadness.lunar.console;

import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.BaseList;

/**
 * Created by alementuev on 6/2/15.
 */
public class CVarChangedDelegateList extends BaseList<CVarChangedDelegate>
{
    private static final CVarChangedDelegate NullCVarChangedDelegate = new CVarChangedDelegate()
    {
        @Override
        public void onChanged(CVar cvar)
        {
        }
    };

    public CVarChangedDelegateList(int capacity)
    {
        super(NullCVarChangedDelegate, capacity);
    }

    public synchronized void NotifyValueChanged(CVar cvar)
    {
        try
        {
            lock();

            int elementsCount = list.size();
            for (int i = 0; i < elementsCount; ++i) // do not update added items on that tick
            {
                try
                {
                    list.get(i).onChanged(cvar);
                }
                catch (Exception e)
                {
                    Log.logException(e);
                }
            }
        }
        finally
        {
            unlock();
        }
    }
}

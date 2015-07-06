package com.spacemadness.lunar.core;

/**
 * Created by alementuev on 6/30/15.
 */
public interface TimerListener
{
    void onTimerScheduled(Timer timer);
    void onTimerSuspended(Timer timer);
    void onTimerResumed(Timer timer);
    void onTimerFired(Timer timer);
    void onTimerCancelled(Timer timer);
    void onTimerFinished(Timer timer);
    void onTimerRemoved(Timer timer);
}

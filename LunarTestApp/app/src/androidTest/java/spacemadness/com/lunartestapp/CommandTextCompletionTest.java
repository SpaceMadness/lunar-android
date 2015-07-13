package spacemadness.com.lunartestapp;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.spacemadness.lunar.ui.CommandEditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import spacemadness.com.lunar.R;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.RootMatchers.*;
import static org.hamcrest.Matchers.*;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CommandTextCompletionTest
{
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCommandCompletion()
    {
        // Type "t" to trigger command suggestion
        typeCommandText("t");

        // 'test' command should be suggested
        checkPopupItemDisplayed("test");

        // Select the suggestion
        selectPopupItem("test");

        // Check command line
        checkCommandText("test");
    }

    @Test
    public void testCommandArgCompletion()
    {
        // Type "t" to trigger command suggestion
        typeCommandText("test ");

        // Check options suggestion
        checkPopupItemDisplayed("arg1");
        checkPopupItemDisplayed("arg2");
        checkPopupItemDisplayed("arg3");

        // Select the suggestion
        selectPopupItem("arg2");

        // Check command line
        checkCommandText("test arg2");
    }

    @Test
    public void testCommandBooleanOptionCompletion()
    {
        // Show options
        typeCommandText("test -");

        // Check options suggestion
        checkPopupItemDisplayed("-o1");
        checkPopupItemDisplayed("-o2");
        checkPopupItemDisplayed("-o3");

        // Select option
        selectPopupItem("-o1");

        // Check command line
        checkCommandText("test -o1");
    }

    @Test
    public void testCommandBooleanOptionAndArgsCompletion()
    {
        // Show options
        typeCommandText("test -o1 ");

        // Check options suggestion
        checkPopupItemDisplayed("arg1");
        checkPopupItemDisplayed("arg2");
        checkPopupItemDisplayed("arg3");

        // Select option
        selectPopupItem("arg2");

        // Check command line
        checkCommandText("test -o1 arg2");
    }

    @Test
    public void testCommandOptionWithValuesCompletion()
    {
        // Show options
        typeCommandText("test -o3 ");

        // Check options suggestion
        checkPopupItemDisplayed("val1");
        checkPopupItemDisplayed("val2");
        checkPopupItemDisplayed("val3");
    }

    @Before
    public void setup()
    {
        CommandEditText.clearHistory(getActivity());
        sleep(250); // give activity a chance to start
    }

    private void typeCommandText(String text)
    {
        onView(withId(R.id.edit_text_command_line))
                .perform(typeText(text));

        sleep(500);
    }

    private void checkCommandText(String text)
    {
        onView(withId(R.id.edit_text_command_line))
                .check(matches(withText(text)));
    }

    private void checkPopupItemDisplayed(String text)
    {
        onData(allOf(instanceOf(String.class), is(text)))
                .inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    private void selectPopupItem(String text)
    {
        onData(allOf(instanceOf(String.class), is(text)))
                .inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView()))))
                .perform(click());
    }

    private void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
        }
    }

    private Activity getActivity()
    {
        return activityRule.getActivity();
    }
}

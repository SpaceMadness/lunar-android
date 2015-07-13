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
    public void testHistoryButton()
    {
        // Type "t" to trigger command suggestion
        typeCommandText("t");

        // Check that suggestion is displayed
        checkPopupItemDisplayed("test");

        // Select the suggestion
        selectPopupItem("test");

        // Command should be filled in
        checkCommandText("test");

        // Show options autocompletion
        typeCommandText(" -");

        // Check short options suggested
        checkPopupItemDisplayed("-o1");
        checkPopupItemDisplayed("-o2");
        checkPopupItemDisplayed("-o3");

        // Select option
        selectPopupItem("-o1");

        // Check new text
        checkCommandText("test -o1");
    }

    @Before
    public void setup()
    {
        CommandEditText.clearHistory(getActivity());
    }

    private void executeCommands(String... commands)
    {
        for (String command : commands)
        {
            executeCommand(command);
        }
    }

    private void typeCommandText(String text)
    {
        onView(withId(R.id.edit_text_command_line))
                .perform(typeText(text), ViewActions.closeSoftKeyboard());

        sleep(500);
    }

    private void checkCommandText(String text)
    {
        onView(withId(R.id.edit_text_command_line))
                .check(matches(withText(text)));
    }

    private void executeCommand(String command)
    {
        onView(withId(R.id.edit_text_command_line)).
                perform(typeText(command), // type command
                        pressImeActionButton(), // hit "Run"
                        ViewActions.closeSoftKeyboard() // close
                );
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

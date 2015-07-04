package spacemadness.com.lunartestapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.utils.StringUtils;

public class MainActivity extends ActionBarActivity
{
    private static final CVar c_bool = new CVar("c_bool", true);
    private static final CVar c_int = new CVar("c_int", 10);
    private static final CVar c_float = new CVar("c_float", 3.14f);
    private static final CVar c_string = new CVar("c_string", "Some string");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringUtils.colorsDisabled = true; // FIXME: remove

        AppTerminal.initialize(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        AppTerminal.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

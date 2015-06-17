package spacemadness.com.lunartestapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setEditText();
        setButtons();
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

    private void setEditText()
    {
        EditText editText = (EditText) findViewById(R.id.edit_text_command_line);
    }

    private void setButtons()
    {
        setClickListener(R.id.button_up, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(R.id.button_down, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(R.id.button_left, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(R.id.button_right, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(R.id.button_clear, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(R.id.button_run, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
    }

    private void setClickListener(int id, View.OnClickListener listener)
    {
        findViewById(id).setOnClickListener(listener);
    }
}

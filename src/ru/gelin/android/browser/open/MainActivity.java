package ru.gelin.android.browser.open;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends ListActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button resetButton = (Button)findViewById(R.id.btnReset);
        resetButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListAdapter adapter = new BrowsersAdapter(this);
        setListAdapter(adapter);
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnReset:
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            adapter.getManager().setSelected(-1);
			setListAdapter(adapter); //way to refresh view
			break;
		}
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            adapter.getManager().setSelected(position);
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
        }
    }

}

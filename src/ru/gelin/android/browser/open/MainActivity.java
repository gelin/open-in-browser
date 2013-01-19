package ru.gelin.android.browser.open;

import ru.gelin.android.browser.open.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	Button but_apply;
	Button but_exit;
	Button but_reset;
	ListView lvMain;

	OibPreferenceManager man;
	BrowsersAdapter adapter;
	
	String ID_SELECTION;
	String def_package;
	
	int selected_pos;
	int items_count;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ID_SELECTION = getString(R.string.oib_id_selection);

        but_apply = (Button)findViewById(R.id.btnApply);
        but_reset = (Button)findViewById(R.id.btnReset);
        but_exit = (Button)findViewById(R.id.btnExit);
        lvMain = (ListView)findViewById(R.id.list_main);
        
        but_apply.setOnClickListener(this);
        but_reset.setOnClickListener(this);
        but_exit.setOnClickListener(this);
              
        man = new OibPreferenceManager(this,ID_SELECTION);
        
        selected_pos = -1;
        
            adapter = new BrowsersAdapter(this);
            items_count = adapter.getCount();
     
            if (items_count == 0) { //no activities
            	lvMain.setVisibility(View.GONE);
            	but_apply.setVisibility(View.GONE);
            	but_reset.setVisibility(View.GONE);
            	TextView tevv = (TextView)findViewById(R.id.text_noitems);
            	tevv.setVisibility(View.VISIBLE);
            } else { //
            	lvMain.setOnItemClickListener(this);
            	lvMain.setOnItemLongClickListener(this);
            	lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            	lvMain.setAdapter(adapter);
            	
            	def_package = man.loadSelection();
            	Log.d(Tag.TAG,"default package = "+def_package);
            	
            	for (int item_pos=0; item_pos<items_count; item_pos++) {
            		if (def_package.equals(adapter.getBrowserPackage(item_pos))) {
            			adapter.setChecked(item_pos);
            			lvMain.performItemClick(lvMain, item_pos, 0);           			
            			break;
            		}
            	}
            	
            }       
    }
   
	@Override
	public void onClick(View v) {
		Log.d(Tag.TAG,"color ="+lvMain.getSolidColor());
		switch (v.getId()) {
		case R.id.btnApply:
			try {
				int pos = lvMain.getCheckedItemPosition();
				adapter.setChecked(pos);
				lvMain.performItemClick(lvMain, pos, 0);
				man.saveSelection(adapter.getBrowserPackage(pos));
			} catch (Exception ex) {
				Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnReset:
			int pos = lvMain.getCheckedItemPosition();
			adapter.uncheck();
			man.saveSelection("");
			lvMain.performItemClick(lvMain, pos, 0);
			break;
		case R.id.btnExit:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
		Log.d(Tag.TAG,"id="+v.getId()+" arg3="+arg3);
		
		int pos = lvMain.getCheckedItemPosition();
		adapter.setSelected(pos);
		//lvMain.smoothScrollToPosition(pos);
	}
	
	@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			adapter.setChecked(position);
			lvMain.performItemClick(lvMain, position, 0);
			man.saveSelection(adapter.getBrowserPackage(position));
		} catch (Exception ex) {
			Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
}

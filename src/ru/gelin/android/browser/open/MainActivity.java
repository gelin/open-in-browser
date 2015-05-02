package ru.gelin.android.browser.open;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ru.gelin.android.browser.open.donate.DonateStatus;
import ru.gelin.android.browser.open.donate.DonateStatusListener;
import ru.gelin.android.browser.open.donate.Donation;

public class MainActivity extends ListActivity implements DonateStatusListener {

    static final int REQUEST_CODE = 1;

    Donation donation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.donation = new Donation(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.donation != null) {
            this.donation.destroy();
            this.donation = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListAdapter adapter = new BrowserOptionsAdapter(this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            BrowsersListManager manager = adapter.getManager();
            manager.setSelected(position);
            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
        }
    }

    public void onDonateButtonClick(View v) {
        v.setEnabled(false);
        startDonatePurchase();
    }

    void updateDonateView(DonateStatus status) {
        View button = findViewById(R.id.donate_button);
        if (button == null) {
            return;
        }
        switch (status) {
            case NONE:
            case PURCHASED:
                button.setEnabled(false);
                button.setVisibility(View.GONE);
                break;
            case EXPECTING:
                button.setEnabled(true);
                button.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDonateStatusChanged(DonateStatus status) {
        updateDonateView(status);
        switch (status) {
            case PURCHASED:
                Toast.makeText(this, R.string.donate_thanks, Toast.LENGTH_LONG).show();
                break;
        }
    }

    void startDonatePurchase() {
        if (this.donation == null) {
            return;
        }
        PendingIntent intent = this.donation.getPurchaseIntent();
        if (intent == null) {
            return;
        }
        try {
            startIntentSenderForResult(intent.getIntentSender(), REQUEST_CODE, new Intent(), 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w(Tag.TAG, "startIntentSenderForResult() failed", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (resultCode != RESULT_OK) {
            updateDonateView(DonateStatus.EXPECTING);
            return;
        }
        if (this.donation == null) {
            return;
        }
        this.donation.processPurchaseResult(data);
    }

}

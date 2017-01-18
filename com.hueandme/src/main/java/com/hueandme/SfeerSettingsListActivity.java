package com.hueandme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hueandme.sfeer.SfeerConfigurationController;
import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SfeerSettingsListActivity extends AppCompatActivity {

    private SfeerConfigurationController mConfigurationController;
    private ArrayAdapter<SfeerConfiguration> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings_list);

        setTitle(R.string.nav_personal_atmosphere);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SfeerSettingsListActivity.this, SfeerSettingsActivity.class));
            }
        });

        mConfigurationController = new SfeerConfigurationController(this);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView lstSettings = (ListView) findViewById(R.id.lst_settings);
        lstSettings.setEmptyView(findViewById(android.R.id.empty));
        lstSettings.setAdapter(mAdapter);

        lstSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SfeerSettingsListActivity.this, SfeerSettingsActivity.class);
                intent.putExtra("name", mAdapter.getItem(position).getName());
                startActivity(intent);
            }
        });

        lstSettings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final SfeerConfiguration configuration = mAdapter.getItem(position);

                new AlertDialog.Builder(SfeerSettingsListActivity.this)
                        .setTitle("Remove " + configuration.getName() + "?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mConfigurationController.remove(configuration);
                                reloadList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadList();
    }

    private void reloadList() {
        final List<SfeerConfiguration> configurations = new ArrayList<>(mConfigurationController.getAll().values());
        mAdapter.clear();
        mAdapter.addAll(configurations);
    }
}

package com.hueandme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SfeerSettingsListActivity.this, SfeerSettingsActivity.class));
            }
        });

        mConfigurationController = new SfeerConfigurationController(this);

        final List<SfeerConfiguration> configurations = new ArrayList<>(mConfigurationController.getAll().values());

        final ArrayAdapter<SfeerConfiguration> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, configurations);

        ListView lstSettings = (ListView) findViewById(R.id.lst_settings);
        lstSettings.setEmptyView(findViewById(android.R.id.empty));
        lstSettings.setAdapter(adapter);

        lstSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SfeerSettingsListActivity.this, SfeerSettingsActivity.class);
                intent.putExtra("name", adapter.getItem(position).getName());
                startActivity(intent);
            }
        });
    }
}

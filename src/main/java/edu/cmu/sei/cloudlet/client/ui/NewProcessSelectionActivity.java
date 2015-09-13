/*
KVM-based Discoverable Cloudlet (KD-Cloudlet) 
Copyright (c) 2015 Carnegie Mellon University.
All Rights Reserved.

THIS SOFTWARE IS PROVIDED "AS IS," WITH NO WARRANTIES WHATSOEVER. CARNEGIE MELLON UNIVERSITY EXPRESSLY DISCLAIMS TO THE FULLEST EXTENT PERMITTEDBY LAW ALL EXPRESS, IMPLIED, AND STATUTORY WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF PROPRIETARY RIGHTS.

Released under a modified BSD license, please see license.txt for full terms.
DM-0002138

KD-Cloudlet includes and/or makes use of the following Third-Party Software subject to their own licenses:
MiniMongo
Copyright (c) 2010-2014, Steve Lacy 
All rights reserved. Released under BSD license.
https://github.com/MiniMongo/minimongo/blob/master/LICENSE

Bootstrap
Copyright (c) 2011-2015 Twitter, Inc.
Released under the MIT License
https://github.com/twbs/bootstrap/blob/master/LICENSE

jQuery JavaScript Library v1.11.0
http://jquery.com/
Includes Sizzle.js
http://sizzlejs.com/
Copyright 2005, 2014 jQuery Foundation, Inc. and other contributors
Released under the MIT license
http://jquery.org/license
*/
package edu.cmu.sei.cloudlet.client.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.cmu.sei.ams.cloudlet.android.CredentialsManager;
import edu.cmu.sei.cloudlet.client.R;
import edu.cmu.sei.cloudlet.client.push.ui.AppListActivity;
import edu.cmu.sei.cloudlet.client.security.WifiProfileManager;

/**
 * User: jdroot
 * Date: 4/15/14
 * Time: 10:43 AM
 */
public class NewProcessSelectionActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_process_selection);

        final Button cloudletButton = (Button) findViewById(R.id.cloudlet_selection);
        final Button appButton = (Button) findViewById(R.id.app_selection);
        final Button pairingButton = (Button) findViewById(R.id.pairing_selection);
        final Button wifiConnectButton = (Button) findViewById(R.id.wifi_connect_button);

        //Make the old cloudlet app run
        cloudletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewProcessSelectionActivity.this, CloudletDiscoveryActivity.class);
                startActivity(i);
            }
        });

        //cloudletButton.setVisibility(View.GONE);

        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(NewProcessSelectionActivity.this, AppListActivity.class);
                startActivity(i);
            }
        });

        //Start the service selection activity
        pairingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(NewProcessSelectionActivity.this, PairingActivity.class);
                startActivity(i);
            }
        });

        //Start the service selection activity
        wifiConnectButton.setOnClickListener(new View.OnClickListener()
        {
            final String TAG = "WifiConnectButton";

            @Override
            public void onClick(View view)
            {
                try {
                    String networkId = CredentialsManager.loadDataFromFile("ssid");
                    String serverCertificatePath = CredentialsManager.loadDataFromFile("radius_cert_path");
                    String password = CredentialsManager.loadDataFromFile("password");

                    int netId = WifiProfileManager.setupWPA2WifiProfile(networkId, serverCertificatePath,
                                        CredentialsManager.getDeviceId(NewProcessSelectionActivity.this), password, NewProcessSelectionActivity.this);
                    Log.v(TAG, "Wi-Fi profile successfully created.");

                    // Connect to network.
                    WifiManager wifiManager = (WifiManager) NewProcessSelectionActivity.this.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.enableNetwork(netId, true);

                } catch (Exception e) {
                    Log.e(TAG, "Error creating Wi-Fi profile.");
                    e.printStackTrace();
                }
            }
        });

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets.android;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
    formUri = "https://30secondsnippets.cloudant.com/acra-snippets/_design/acra-storage/_update/report",
    reportType = HttpSender.Type.JSON,
    httpMethod = HttpSender.Method.POST,
    formUriBasicAuthLogin = "theretstattlandendartedr",
    formUriBasicAuthPassword = "B0B6M28knWGEpPDbA2PgUWPh",
    formKey = "", // This is required for backward compatibility but not used
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.toast_crash
    
)

public class Reporter extends Application{
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();

    }
}

package com.mahesh.vitacm;

/**
 * Created by Mahesh on 1/1/14.
 */

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = "http://vitmumbai.acm.org/app/logs/report.php"
)
public class VITACMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}

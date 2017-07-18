package com.softmine.dooktravel.serviceconnection;

import android.content.Context;

/**
 * Created by GAURAV on 7/8/2017.
 */

public interface CompleteListener {

    public void done(String response);
    Context getApplicationsContext();
}

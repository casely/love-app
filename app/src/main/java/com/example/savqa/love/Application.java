package com.example.savqa.love;

import com.parse.Parse;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "le5Itp17zPj09YXzeSINDfKbxrSlVYCmjZtiqCYD", "LKKP6OquHCWiqq1I3fYVSEUSnT2I0qA4mrnwBOHX");
    }
}

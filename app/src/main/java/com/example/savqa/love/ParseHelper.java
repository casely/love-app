package com.example.savqa.love;

import com.parse.ParseUser;

public class ParseHelper extends ParseUser {
    public ParseHelper() {}

    public void setGender(int value) {
        put("gender", value);
    }

    public int getGender() {
        return getInt("gender");
    }
}

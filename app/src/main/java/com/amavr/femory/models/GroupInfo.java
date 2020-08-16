package com.amavr.femory.models;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
    public String key;
    public transient Object ref;
    public String name;
    public List<ItemInfo> items = new ArrayList<>();
}

package com.tinnhantet.nhantin.hengio.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tinnhantet.nhantin.hengio.models.Contact;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String listContactString(List<Contact> contacts) {
        return new Gson().toJson(contacts);
    }

    public static List<Contact> getAllContact(String contacts) {
        Type listType = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        return new Gson().fromJson(contacts, listType);
    }

    public static String getAllNameContact(List<Contact> contacts) {
        if (contacts == null) {
            return "";
        }
        int size = contacts.size();
        String listName = "";
        for (int i = 0; i < size; i++) {
            Contact contact = contacts.get(i);
            String name = contact.getName();
            if (name.equals("")) {
                name = contact.getPhone();
            }
            listName += name + ", ";
        }
        listName = listName.substring(0, listName.length() - 2);
        return listName;
    }
}

package com.tinnhanchuctet.loichuchay.chuctet.models;

import com.tinnhanchuctet.loichuchay.chuctet.R;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int mId;
    private String mName;
    private int mIcon;

    public int getCategoryBg() {
        return mCategoryBg;
    }

    public Category setCategoryBg(int categoryBg) {
        mCategoryBg = categoryBg;
        return this;
    }

    private int mCategoryBg;

    public Category(int id, String name, int icon, int categoryBg) {
        mId = id;
        mName = name;
        mIcon = icon;
        mCategoryBg = categoryBg;
    }

    public int getId() {
        return mId;
    }

    public Category setId(int id) {
        mId = id;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Category setName(String name) {
        mName = name;
        return this;
    }

    public int getIcon() {
        return mIcon;
    }

    public Category setIcon(int icon) {
        mIcon = icon;
        return this;
    }

    public static List<Category> initCategory() {
        List<Category> categories = new ArrayList<>();
        Integer[] mArraysId = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        Integer[] mArraysBG = new Integer[]{R.drawable.select_two_ef, R.drawable.select_three_ef,
                R.drawable.select_two_ef, R.drawable.select_three_ef
                , R.drawable.select_two_ef, R.drawable.select_three_ef,
                R.drawable.select_two_ef};
        Integer[] mArraysIcon = new Integer[]{R.drawable.ic_loichuchay, R.drawable.ic_ongdo,
                R.drawable.ic_giadinh, R.drawable.ic_thayco, R.drawable.ic_sep,
                R.drawable.ic_nguoiyeu, R.drawable.ic_cute};
        String[] mArraysName = new String[]{"Lời chúc tết hay và ý nghĩa", "Ông Đồ chúc tết", "Lời chúc tết gia đình"
                , "Chúc tết thầy cô", "Chúc tết sếp", "Chúc tết người yêu, vợ chồng",
                "Lời chúc tết cute"};
        for (int i = 0; i < mArraysIcon.length; i++) {
            categories.add(new Category(mArraysId[i], mArraysName[i], mArraysIcon[i], mArraysBG[i]));
        }

        return categories;
    }
}

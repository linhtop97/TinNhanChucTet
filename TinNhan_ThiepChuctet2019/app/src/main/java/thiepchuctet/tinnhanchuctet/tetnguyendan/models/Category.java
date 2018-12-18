package thiepchuctet.tinnhanchuctet.tetnguyendan.models;

import java.util.ArrayList;
import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;

public class Category {

    private int mId;
    private String mName;
    private int mIcon;

    public Category(int id, String name, int icon) {
        mId = id;
        mName = name;
        mIcon = icon;
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
        Integer[] mArraysId = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer[] mArraysIcon = new Integer[]{R.drawable.ic_flash_active, R.drawable.ic_hd_active,
                R.drawable.ic_ltr, R.drawable.ic_flash_active, R.drawable.ic_hd_active,
                R.drawable.ic_ltr, R.drawable.ic_flash_active, R.drawable.ic_hd_active, R.drawable.ic_ltr};
        String[] mArraysName = new String[]{"Lời chúc tết hay và ý nghĩa", "Ông Đồ chúc tết", "lời chúc tết ông bà",
                "chúc tết bố mẹ", "Chúc tết thầy cô", "Chúc tết người yêu, vợ chồng", "Chúc tết sếp",
                "Tin nhắn hình", "Lời chúc tết cute"};
        for (int i = 0; i < mArraysIcon.length; i++) {
            categories.add(new Category(mArraysId[i], mArraysName[i], mArraysIcon[i]));
        }

        return categories;
    }
}

package thiepchuctet.tinnhanchuctet.tetnguyendan.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;

public final class BindingUtils {

    private BindingUtils() {
    }

    @BindingAdapter({"app:setIconCategory"})
    public static void setIconCategory(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }

    @BindingAdapter({"app:setNumOfMsg"})
    public static void setNumOfMsg(TextView textView, int num) {
        textView.setText(textView.getResources().getString(R.string.num_of_msg).concat(" " + num));
    }
}

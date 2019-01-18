package com.tinnhanchuctet.loichuchay.chuctet.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.ItemCategoryBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnCategoryClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private OnCategoryClickListener mCategoryClickListener;
    private List<Category> mCategoryList;
    private Context mCxt;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        mCxt = context;
        mCategoryList = categoryList;
    }

    public void setOnItemClick(OnCategoryClickListener listener) {
        mCategoryClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_category, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindData(mCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategoryList != null ? mCategoryList.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ItemCategoryBinding mCategoryBinding;

        private CategoryViewHolder(ItemCategoryBinding itemView) {
            super(itemView.getRoot());
            mCategoryBinding = itemView;

        }

        private void bindData(Category category) {
            if (category == null) {
                return;
            }
            Glide.with(mCxt)
                    .load(category.getIcon())
                    .into(mCategoryBinding.imgMsgMine);
            Glide.with(mCxt)
                    .load(category.getCategoryBg())
                    .into(mCategoryBinding.imgBackground);
            Typeface font = Typeface.createFromAsset(mCxt.getAssets(), "fonts/font_tieude.otf");
            mCategoryBinding.txtCategoryName.setTypeface(font);
            mCategoryBinding.setCategory(category);
            mCategoryBinding.setListener(mCategoryClickListener);
            mCategoryBinding.executePendingBindings();
        }
    }
}

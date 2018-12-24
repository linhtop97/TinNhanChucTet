package com.tinnhantet.loichuc.chuctet.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.databinding.ItemCategoryBinding;
import com.tinnhantet.loichuc.chuctet.listeners.OnCategoryClickListener;
import com.tinnhantet.loichuc.chuctet.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private OnCategoryClickListener mListener;
    private List<Category> mCategories;
    private Context mContext;

    public CategoryAdapter(Context context, List<Category> categories) {
        mContext = context;
        mCategories = categories;
    }

    public void setOnItemClick(OnCategoryClickListener listener) {
        mListener = listener;
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
        holder.bindData(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ItemCategoryBinding mBinding;

        private CategoryViewHolder(ItemCategoryBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;

        }

        private void bindData(Category category) {
            if (category == null) {
                return;
            }
//            Glide.with(mContext)
//                    .load(R.drawable.select_category_ef)
//                    .into(mBinding.imgBackground);
            Glide.with(mContext)
                    .load(category.getIcon())
                    .into(mBinding.imgMsgMine);
            mBinding.setCategory(category);
            mBinding.setListener(mListener);
            mBinding.executePendingBindings();
        }
    }
}

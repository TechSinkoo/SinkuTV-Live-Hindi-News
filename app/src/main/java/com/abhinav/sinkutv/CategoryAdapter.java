package com.abhinav.sinkutv;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>  {

    private List<CategoryModel> categoryModelList;



    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(categoryModelList.get(position).getUrl(),categoryModelList.get(position).getImageurl(),categoryModelList.get(position).getName(),categoryModelList.get(position).getWebsiteurl(),position);
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }







    class ViewHolder extends RecyclerView.ViewHolder{
        //private CircleImageView imageView;
        ImageView imageView;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);

        }
        private void setData(String url,String imageurl, final String title,String websiteurl, final int position )
        {
            Glide.with(itemView.getContext()).load(imageurl).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent setIntent = new Intent(itemView.getContext(),VideoplayActivity.class);
                    setIntent.putExtra("vdokey",url);
                    setIntent.putExtra("web",websiteurl);
                    itemView.getContext().startActivity(setIntent);
                    //setIntent.putExtra("position",position);

//

                }
            });



        }
    }
}

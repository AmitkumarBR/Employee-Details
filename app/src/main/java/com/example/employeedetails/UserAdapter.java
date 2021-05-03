package com.example.employeedetails;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeedetails.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    Context context;
    List<UserModel> userModelList;
    List<UserModel> searchList;

    public UserAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
        searchList = new ArrayList<>(userModelList);
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_design, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);
        holder.tvName.setText("Name:" + userModel.getName());
        holder.tvId.setText("Id:" + userModel.getId());
        holder.tvCompany.setText("Company:" + userModel.getCompany());

        String imageUri = null;
        imageUri = userModel.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);

        holder.imageEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.edit))
                        .setExpanded(false)
                        .create();

                View v = dialog.getHolderView();

                ImageView updateImage = v.findViewById(R.id.update_image);
                EditText updateName = v.findViewById(R.id.update_name);
                EditText updateId = v.findViewById(R.id.update_id);
                EditText updateCompany = v.findViewById(R.id.update_company);
                Button updateButton = v.findViewById(R.id.update_button);

                updateName.setText(userModel.getName());
                updateId.setText(userModel.getId());
                updateCompany.setText(userModel.getCompany());

                String imageUri = null;
                imageUri = userModel.getImage();
                Picasso.get().load(imageUri).into(updateImage);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("Name", updateName.getText().toString());
                        map.put("Id", updateId.getText().toString());
                        map.put("Company", updateCompany.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("User Details")
                                .child(userModel.getId())
                                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                dialog.show();
            };
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("User Details").child(userModel.getId())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }

    private Filter FilterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String searchText = charSequence.toString().toLowerCase();
            List<UserModel> tempList = new ArrayList<>();
            if (searchText.length() == 0 || searchText.isEmpty()) {
                tempList.addAll(searchList);
            } else {
                for (UserModel item:searchList) {
                    if (item.getName().toLowerCase().contains(searchText)) {
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userModelList.clear();
            userModelList.addAll((Collection<? extends UserModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView tvName, tvId, tvCompany;
        ImageView imageEdit, imageDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_user);
            tvName = itemView.findViewById(R.id.txt_name);
            tvId = itemView.findViewById(R.id.txt_id);
            tvCompany = itemView.findViewById(R.id.txt_company);

            imageEdit = itemView.findViewById(R.id.image_edit);
            imageDelete = itemView.findViewById(R.id.image_delete);
        }
    }
}

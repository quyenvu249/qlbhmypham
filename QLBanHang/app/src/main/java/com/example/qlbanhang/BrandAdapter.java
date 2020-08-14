package com.example.qlbanhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrandAdapter extends BaseAdapter {
    Context context;
    List<Brand> arrayList;
    int myLayout;

    public BrandAdapter(Context context, List<Brand> arrayList, int myLayout) {
        this.context = context;
        this.arrayList = arrayList;
        this.myLayout = myLayout;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }


    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgTH, btnSua, btnXoa;
        TextView tenTH;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(myLayout, null);
            viewHolder.tenTH = (TextView) convertView.findViewById(R.id.tvTenTH);
            viewHolder.imgTH = (ImageView) convertView.findViewById(R.id.imgTH);
            viewHolder.btnSua = (ImageView) convertView.findViewById(R.id.btnSua);
            viewHolder.btnXoa = (ImageView) convertView.findViewById(R.id.btnXoa);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBrandActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("brID", arrayList.get(position).brID);
                bundle.putString("brName", arrayList.get(position).brName);
                bundle.putString("brLink", arrayList.get(position).brLink);
                intent.putExtra("bdBr", bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Xóa thương hiệu?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference("Brands").child(arrayList.get(position).brID).removeValue();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        arrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        viewHolder.tenTH.setText(arrayList.get(position).brName);
        Picasso.get().load(arrayList.get(position).brLink).into(viewHolder.imgTH);
        return convertView;
    }
}
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

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    Context context;
    List<Product> arrayList;
    int myLayout;

    public ProductAdapter(Context context, List<Product> arrayList, int myLayout) {
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
        ImageView imgSP,btnSua,btnXoa;
        TextView tenSP,NSX, HSD, giaBan;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(myLayout, null);
            viewHolder.tenSP = (TextView) convertView.findViewById(R.id.tvTenSP);
            viewHolder.NSX = (TextView) convertView.findViewById(R.id.tvNSX);
            viewHolder.HSD = (TextView) convertView.findViewById(R.id.tvHSD);
            viewHolder.giaBan = (TextView) convertView.findViewById(R.id.tvGiaBan);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.btnSua = (ImageView) convertView.findViewById(R.id.btnSua);
            viewHolder.btnXoa = (ImageView) convertView.findViewById(R.id.btnXoa);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView
                    .getTag();
        }
        viewHolder.tenSP.setText(arrayList.get(position).proName);
        viewHolder.NSX.setText(arrayList.get(position).NSX);
        viewHolder.HSD.setText(arrayList.get(position).HSD);
        viewHolder.giaBan.setText("" + arrayList.get(position).imPrice * 1.3);
        Picasso.get().load(arrayList.get(position).proLink).into(viewHolder.imgSP);
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("proID", arrayList.get(position).proID);
                bundle.putString("proName", arrayList.get(position).proName);
                bundle.putString("proLink", arrayList.get(position).proLink);
                bundle.putString("proBrand", arrayList.get(position).proBrand);
                bundle.putString("NSX", arrayList.get(position).NSX);
                bundle.putString("HSD", arrayList.get(position).HSD);
                bundle.putString("proDes", arrayList.get(position).proDes);
                bundle.putDouble("imPrice",arrayList.get(position).imPrice);
                intent.putExtra("bdPr", bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Xóa sản phẩm?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference("Products").child(arrayList.get(position).proID).removeValue();
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
        return convertView;
    }
}


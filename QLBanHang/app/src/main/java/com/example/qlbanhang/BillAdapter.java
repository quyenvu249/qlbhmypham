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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BillAdapter extends BaseAdapter {
    Context context;
    List<Bill> list;
    int layout;


    public BillAdapter(Context context, List<Bill> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvTenHD, tvTenKH, tvDate, tvTongTien;
        ImageView btnSua, btnXoa;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(layout, null);
            viewHolder.tvTenHD = (TextView) convertView.findViewById(R.id.tvTenHD);
            viewHolder.tvTenKH = (TextView) convertView.findViewById(R.id.tvKH);
            viewHolder.tvTongTien = (TextView) convertView.findViewById(R.id.tvTongTien);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.btnSua = convertView.findViewById(R.id.btnSua);
            viewHolder.btnXoa = convertView.findViewById(R.id.btnXoa);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTenHD.setText(list.get(position).billName);
        viewHolder.tvTenKH.setText(list.get(position).billCus);
        viewHolder.tvTongTien.setText(list.get(position).billTotal+"");
        viewHolder.tvDate.setText(list.get(position).billTime);
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Sửa hóa đơn");
                final View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_update_bill, null);
                final EditText kh = dialog.findViewById(R.id.edTenKH);
                final EditText total = dialog.findViewById(R.id.edTongTien);
                Button btnSuaHD = dialog.findViewById(R.id.btnUpdateHD);
                btnSuaHD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bill bill = new Bill(list.get(position).billID, list.get(position).billName, list.get(position).billCus, kh.getText().toString(), list.get(position).billTime, Double.parseDouble(total.getText().toString()));
                        FirebaseDatabase.getInstance().getReference("Bills").child(list.get(position).billID).setValue(bill);
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, BillActivity.class));
                    }
                });
                builder.setView(dialog);
                builder.create();
                builder.show();
            }
        });
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Xóa hóa đơn?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference("Bills").child(list.get(position).billID).removeValue();
                        list.remove(position);
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
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

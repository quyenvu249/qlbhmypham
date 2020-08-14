package com.example.qlbanhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DetailBillAdapter extends BaseAdapter {
    Context context;
    List<DetailBill> list;
    int layout;


    public DetailBillAdapter(Context context, List<DetailBill> list, int layout) {
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
        TextView tvTenHD, tvSP, tvTT, tvGC;
        ImageView btnSua, btnXoa;
        Button btnUpdateHDCT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(layout, null);
            viewHolder.tvTenHD = (TextView) convertView.findViewById(R.id.tvTenHD);
            viewHolder.tvSP = (TextView) convertView.findViewById(R.id.tvSP);
            viewHolder.tvTT = (TextView) convertView.findViewById(R.id.tvTT);
            viewHolder.tvGC = (TextView) convertView.findViewById(R.id.tvGC);
            viewHolder.btnSua = (ImageView) convertView.findViewById(R.id.btnSua);
            viewHolder.btnXoa = (ImageView) convertView.findViewById(R.id.btnXoa);
            viewHolder.btnUpdateHDCT = (Button) convertView.findViewById(R.id.btnUpdateHDCT);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DetailBillAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Sửa hóa đơn chi tiết");
                View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_update_detailbill, null);
                final EditText sl = dialog.findViewById(R.id.edSL);
                final EditText dg = dialog.findViewById(R.id.edĐG);
                final EditText sale = dialog.findViewById(R.id.edSale);
                final EditText gc = dialog.findViewById(R.id.edGC);
                Button btnTT = dialog.findViewById(R.id.btnTT);
                final TextView tvTT = dialog.findViewById(R.id.tvTT);
                Button btnUpdateHDCT = dialog.findViewById(R.id.btnUpdateHDCT);
                btnTT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvTT.setText(Integer.parseInt(sl.getText().toString()) * Double.parseDouble(dg.getText().toString()) - Integer.parseInt(sl.getText().toString()) * Double.parseDouble(dg.getText().toString()) * Double.parseDouble(sale.getText().toString()) + "");
                    }
                });
                btnUpdateHDCT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailBill detailBill = new DetailBill(list.get(position).id, list.get(position).billName, list.get(position).billPr, Integer.parseInt(sl.getText().toString()), Double.parseDouble(dg.getText().toString()), Double.parseDouble(sale.getText().toString()), Double.parseDouble(tvTT.getText().toString()), gc.getText().toString());
                        FirebaseDatabase.getInstance().getReference("DetailBills").child(list.get(position).id).setValue(detailBill);
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, DetailBillActivity.class));
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
                builder.setMessage("Xóa hóa đơn chi tiết?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference("DetailBills").child(list.get(position).id).removeValue();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        list.remove(position);
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
        viewHolder.tvTenHD.setText(list.get(position).billName);
        viewHolder.tvSP.setText(list.get(position).billPr);
        viewHolder.tvTT.setText(list.get(position).totalAmount + "");
        viewHolder.tvGC.setText(list.get(position).note);
        return convertView;
    }
}

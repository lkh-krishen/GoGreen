package com.pulps.gogreen.ViewHolder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.pulps.gogreen.Model.Item;
import com.pulps.gogreen.R;

public class PerchasedListAdapter extends FirebaseRecyclerAdapter <Item, PerchasedListAdapter.PerchasedAdapter>{

    public PerchasedListAdapter(@NonNull FirebaseRecyclerOptions<Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PerchasedAdapter perchasedAdapter, int i, @NonNull Item item) {

        perchasedAdapter.item.setText(item.getItem());
        perchasedAdapter.price.setText(String.valueOf(item.getPrice()));
        perchasedAdapter.weight.setText(String.valueOf(item.getWeight()));
        perchasedAdapter.total.setText(String.valueOf(item.getTotal()));
        perchasedAdapter.address.setText(item.getAddress());
        perchasedAdapter.bank.setText(item.getBank());
        perchasedAdapter.account.setText(String.valueOf(item.getAccount()));
        perchasedAdapter.email.setText(item.getEmail());
        perchasedAdapter.mobile.setText(String.valueOf(item.getMobile()));

        perchasedAdapter.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(perchasedAdapter.item.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can not be undone!");

                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("PerchasedList")
                                .child(getRef(perchasedAdapter.getAdapterPosition()).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(perchasedAdapter.item.getContext(), "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public PerchasedAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.perchased_list_layout,parent,false);
        return new PerchasedAdapter(v);
    }

    class PerchasedAdapter extends RecyclerView.ViewHolder{

        TextView item,price,bank,account,address,email,mobile,weight,total;
        Button deleteBtn;

        public PerchasedAdapter(@NonNull View itemView) {
            super(itemView);

            //getting the reference of the textViews of recycleView
            item=itemView.findViewById(R.id.perItem);
            price=itemView.findViewById(R.id.perPrice);
            bank=itemView.findViewById(R.id.perBank);
            account=itemView.findViewById(R.id.perAccount);
            address=itemView.findViewById(R.id.perAddress);
            email=itemView.findViewById(R.id.perEmail);
            mobile=itemView.findViewById(R.id.perMobile);
            weight=itemView.findViewById(R.id.perWeight);
            total=itemView.findViewById(R.id.perTotal);
            deleteBtn=itemView.findViewById(R.id.perchased_delete_btn);
        }
    }
}

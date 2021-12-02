package com.pulps.gogreen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pulps.gogreen.Model.Item;

public class AddItemActivity extends AppCompatActivity {
    private EditText item,price,bank,account,address,email,mobile,status,weight;
    private Button submit;
    private ProgressDialog loading_bar;
    long id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        item=(EditText) findViewById(R.id.itemName);
        price=(EditText) findViewById(R.id.price);
        bank=(EditText) findViewById(R.id.bank);
        account=(EditText) findViewById(R.id.account);
        address=(EditText) findViewById(R.id.address);
        email=(EditText) findViewById(R.id.email);
        mobile=(EditText) findViewById(R.id.mobile);
        submit=(Button) findViewById(R.id.submit_btn);
        status=(EditText)findViewById(R.id.status);
        weight=(EditText)findViewById(R.id.weight);
        loading_bar=new ProgressDialog(this);

        //make status uneditable
        status.setEnabled(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call sendData function when submit button clicked
                sendData();
            }
        });
    }

    private void sendData() {
        String ItemName = item.getText().toString();
        String Price = price.getText().toString();
        String Bank = bank.getText().toString();
        String Account = account.getText().toString();
        String Address = address.getText().toString();
        String Email = email.getText().toString();
        String Mobile = mobile.getText().toString();
        String Weight=weight.getText().toString();
        final DatabaseReference ref;

        if(TextUtils.isEmpty(ItemName)){
            Toast.makeText(this, "Enter item name please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Enter price please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Bank)){
            Toast.makeText(this, "Enter bank name please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Account)){
            Toast.makeText(this, "Enter account number please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Address)){
            Toast.makeText(this, "Enter address please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Enter email please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Mobile)){
            Toast.makeText(this, "Enter mobile please", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Weight)){
            Toast.makeText(this, "Enter weight please", Toast.LENGTH_SHORT).show();
        }
        else{
            loading_bar.setTitle("Make request");
            loading_bar.setMessage("Your request is being made");
            loading_bar.setCanceledOnTouchOutside(false);
            loading_bar.show();
            validate(item,price,bank,account,address,email,mobile,weight);
        }
    }

    private void validate( EditText item, EditText price, EditText bank, EditText account, EditText address, EditText email, EditText mobile,EditText weight) {

        final DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference().child("Item");
        double total=0;

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id=(snapshot.getChildrenCount());
                }
                Item myItem = new Item();
                myItem.setItem(item.getText().toString());
                myItem.setAccount(Integer.parseInt(account.getText().toString()));
                myItem.setPrice(Integer.parseInt(price.getText().toString()));
                myItem.setBank(bank.getText().toString());
                myItem.setAddress(address.getText().toString());
                myItem.setEmail(email.getText().toString());
                myItem.setMobile(Integer.parseInt(mobile.getText().toString()));
                myItem.setWeight(Integer.parseInt(weight.getText().toString()));
                myItem.setStatus("pending");
                //calculation part of total
                myItem.setTotal(myItem.getWeight()*myItem.getPrice());

                ref.child(String.valueOf(id+1)).setValue(myItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddItemActivity.this, "Request is successfully made", Toast.LENGTH_SHORT).show();
                            loading_bar.dismiss();
                            Intent intent=new Intent(AddItemActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
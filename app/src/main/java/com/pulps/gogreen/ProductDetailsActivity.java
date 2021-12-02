package com.pulps.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.pulps.gogreen.Model.Products;
import com.pulps.gogreen.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productID="", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");                            // Retrieve product ID sent from home activity
        addToCartButton =(Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        getProductDetails(productID);                                                   // Call method to retrieve product details from database
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check state
                // If state is not normal:
                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "You can add Purchase more product, once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                // If state is normal:
                else {
                    addingToCartList(); // Call method to add cart data to database
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState(); // Call method to set state
    }

    // Method to add cart details to database (Can also be used to update quantity of product in the cart)
    private void addingToCartList() {
        String saveCurrentTime,saveCurrentDate;       // Declare variable for current date and time
        Calendar calForDate = Calendar.getInstance(); // Instantiate Calendar object
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());               // Automatically set current date
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());               // Automatically set current time
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List"); // Create reference for cart in database
        final HashMap<String, Object>cartMap = new HashMap<>(); // Create hashmap for current product
        cartMap.put("pid",productID);                           // Insert product ID to hashmap
        cartMap.put("pname",productName.getText().toString());  // Insert product name to hashmap
        cartMap.put("price",productPrice.getText().toString()); // Insert product price to hashmap
        cartMap.put("date",saveCurrentDate);                    // Insert current date to hashmap
        cartMap.put("time",saveCurrentTime);                    // Insert current time to hashmap
        cartMap.put("quantity",numberButton.getNumber());       // Insert product quantity to hashmap
        cartMap.put("discount","");
        cartListRef.child("User view")                            // Go to sub directory under cart called User View or create one if it does not already exist
                   .child(Prevalent.currentOnlineUser.getPhone()) // Go to sub directory under User View for the phone number of the current user or create one if it does not already exist
                   .child("Products")                             // Go to sub directory under the phone number of the current user called Products or create one if it does not already exist
                   .child(productID)                              // Go to sub category under Products for the current product or create one if it does not already exist
                   .updateChildren(cartMap)                       // Insert hashmap of current product (This method can be used to both add the product as well as update its quantity)
                   // Check if the above has been successfully completed
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           // If yes:
                           if (task.isSuccessful()){
                               cartListRef.child("Admin view") // Go to sub directory under cart called Admin View or create one if it does not already exist
                                          .child(Prevalent.currentOnlineUser.getPhone()) // Go to sub directory under Admin View for the phone number of the current user or create one if it does not already exist
                                          .child("Products") // Go to sub directory under the phone number of the current user called Products or create one if it does not already exist
                                          .child(productID) // Go to sub directory under Products for the product ID of the current product or create one if it does not already exist
                                          .updateChildren(cartMap) // Insert hashmap of current product (This method can be used to both add the product as well as update its quantity)
                                          // Check if the above has been successfully completed
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  // If yes:
                                                  if (task.isSuccessful()){
                                                      Toast.makeText(ProductDetailsActivity.this,"Added to cart List",Toast.LENGTH_SHORT).show(); // Display message
                                                      // Redirect to Home Activity
                                                      Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                      startActivity(intent);
                                                  }
                                              }
                                          });
                           }
                       }
                   });
    }

    // Method to retrieve product details from database
    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Method to set state
    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                                                                 .child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    if (shippingState.equals("Shipped")) {
                        state ="Order Shipped";
                    } else if (shippingState.equals("Not Shipped")) {
                        state ="Order Placed";
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}

package com.example.tailorapp.newproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.R;
import com.example.tailorapp.login.UserApI;
import com.example.tailorapp.registration.NewProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AddNewProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddnewProduct";
    private Button color_picker;
    private int r,g,b;
    private View picked_colour_view;
    private ImageButton addPhoto;
    private RecyclerView images_recycler;
    private ProgressBar progressBar;


    private Spinner category_spinner,sub_category_spinner,type_cloth_spinner;
    private EditText new_product_title,new_product_description,new_product_qty;
    private CheckBox xs_checkbox,s_checkbox,m_checkbox,l_checkbox,xl_checkbox,xxl_checkbox,tax_checkbox;
    private EditText new_product_price,new_product_discount,new_product_material_detail;
    private Button new_product_create_button;

    private String title,description,qty,price,discount,material_detail;
    private List<String> checkbox_array = new ArrayList<>();
    private String[] category = {"Fashion","Traditional","Western","Daily"};
    private String[] subcategory = {"Men","Women","Kids"};
    private String[] types_clothes = {"Kurta","Shirt","Pant","Daily","Coat","Jeans","Chudidar"};

    private String Final_category="";
    private String Final_subcategory="";
    private String Final_type_cloth="";
    private List<Uri> uris = new ArrayList<>();
    private int color;

    private String currentUserId;
    private String currentUsername;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Products");
    private StorageReference storageReference;
    private StorageReference filepath;
    private List<String> fire_urls = new ArrayList<>();

    private CheckBox customization, bespoke, embellishment;
    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth!=null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        connection(); //Connection to all fields

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        if (UserApI.getInstance()!= null){
            currentUserId = UserApI.getInstance().getUserId();
            currentUsername = UserApI.getInstance().getUsername();
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        spinner_adapter(); //spinners adapter

        getAllFields(); //getting all the inputs

        color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colour_method(v);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),13);
            }
        });

        new_product_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_create();
            }
        });

//        Alert Dialog

        customization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: "+ buttonView);
                if(isChecked == true){
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(AddNewProduct.this);
                    View popup = getLayoutInflater().inflate(R.layout.popup_customization, null);

//                    method for customization
                    builder.setView(popup);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        bespoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(AddNewProduct.this);
                    View popup = getLayoutInflater().inflate(R.layout.popup_bespoke, null);

//                    method for customization
                    builder.setView(popup);
                    alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });

    }

    private void save_and_create() {
        progressBar.setVisibility(View.VISIBLE);
        getAllFields(); //getting all the inputs

        Log.d(TAG, "save_and_create: "+
                        "Category: "+ Final_category +
                        "Sub_category: "+ Final_subcategory +
                        "Type Cloth: "+ Final_type_cloth +
                        "Title: "+ title +
                        "Description: "+ description +
                        "Stock: "+ qty +
                        "Sizes: "+ checkbox_array.toString() +
                        "Price: "+ price +
                        "Discount: "+ discount +
                        "Material Details: "+ material_detail +
                        "Color: "+ color +
                        "Images: "+ uris.toString()
                );

        if (!uris.isEmpty()){

            for (int i = 0; i <uris.size() ; i++) {
                Uri uri = uris.get(i);
//                filepath.child("i_"+"image");
                int finalI = i;
                filepath = storageReference
                        .child("Product_images")
                        .child("product_"+ currentUsername+ "_"+ currentUserId +"_"+ title +"_")
                        .child("Images_"+ finalI + "_"+ Timestamp.now().getSeconds());
                filepath.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "onSuccess: " + uri + " " + finalI);

                                filepath.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String url = String.valueOf(uri);
                                                fire_urls.add(url);
                                                Log.d(TAG, "onSuccess: Each Url of image: "+ url);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: save File Error" + e.getMessage());
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: save File Error" + e.getMessage());
                            Toast.makeText(AddNewProduct.this, "save File Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        });
            }
            NewProductModel product = new NewProductModel();
            product.setColor(color);
            product.setDescription(description);
            product.setDiscount(discount);
            product.setMaterial_Details(material_detail);
            product.setPrice(price);
            product.setQty(qty);
            product.setSize(checkbox_array);
            product.setSubCategory(Final_subcategory);
            product.setTitle(title);
            product.setType_Cloth(Final_type_cloth);
            product.setCategory(Final_category);
            product.setImage(fire_urls);
            product.setUserId(currentUserId);
            product.setUsername(currentUsername);
            Log.d(TAG, "save_and_create: All url from Firebase"+ fire_urls);

            collectionReference
                    .add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNewProduct.this, "Saved in Firebase", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNewProduct.this, "Failure during saving " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void colour_method(View v) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(AddNewProduct.this);
        View popup = getLayoutInflater().inflate(R.layout.color_picker_dailog, null);
        builder.setView(popup);

        Button save = popup.findViewById(R.id.save_colour);
        TextView r_text,b_text,g_text;
        View show_view = popup.findViewById(R.id.picked_view);

        ImageView imageView = popup.findViewById(R.id.picker_image);
        r_text = popup.findViewById(R.id.r_hex);
        b_text = popup.findViewById(R.id.b_hex);
        g_text = popup.findViewById(R.id.g_hex);


        alertDialog = builder.create();
        alertDialog.show();

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        final Bitmap[] bitmap = new Bitmap[1];
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap[0] = imageView.getDrawingCache();
                    int pixel = bitmap[0].getPixel((int)event.getX(),(int)event.getY());

                    r = Color.red(pixel);
                    b = Color.blue(pixel);
                    g = Color.green(pixel);
                    show_view.setBackgroundColor(Color.rgb(r,g,b));
                    r_text.setText(MessageFormat.format("R: {0}", r));
                    b_text.setText(MessageFormat.format("B: {0}", b));
                    g_text.setText(MessageFormat.format("G: {0}", g));
                }
                return true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Log.d("Colour Picked", "onClick: "+ r + g + b);
                color_picker.setVisibility(View.INVISIBLE);
                picked_colour_view.setVisibility(View.VISIBLE);
                color = Color.rgb(r,g,b);
                picked_colour_view.setBackgroundColor(color);
            }
        });

    }

    private void connection() {
        progressBar = findViewById(R.id.progress_bar_product);
        category_spinner = findViewById(R.id.category_spinner);
        sub_category_spinner = findViewById(R.id.sub_category_spinner);
        type_cloth_spinner = findViewById(R.id.type_cloth_spinner);
        new_product_title = findViewById(R.id.new_product_title);
        new_product_description = findViewById(R.id.new_product_description);
        new_product_qty = findViewById(R.id.new_product_qty);
        xs_checkbox = findViewById(R.id.xs_checkbox);
        s_checkbox = findViewById(R.id.s_checkbox);
        m_checkbox = findViewById(R.id.m_checkbox);
        l_checkbox = findViewById(R.id.l_checkbox);
        xl_checkbox = findViewById(R.id.xl_checkbox);
        xxl_checkbox = findViewById(R.id.xxl_checkbox);
        tax_checkbox = findViewById(R.id.tax_checkbox);
        new_product_price = findViewById(R.id.new_product_price);
        new_product_discount = findViewById(R.id.new_product_discount);
        new_product_material_detail = findViewById(R.id.new_product_material_detail);
        new_product_create_button = findViewById(R.id.new_product_create_button);
        color_picker = findViewById(R.id.new_product_colour_button);
        picked_colour_view = findViewById(R.id.picked_colour_view);
        addPhoto = findViewById(R.id.addPhoto);
        images_recycler = findViewById(R.id.multiple_images);

        customization = findViewById(R.id.checkbox_customization);
        bespoke = findViewById(R.id.checkbox_bespoke);
        embellishment = findViewById(R.id.checkbox_embellishment);
    }

    private void spinner_adapter() {
        ArrayAdapter categrory_adapter = new ArrayAdapter(AddNewProduct.this,R.layout.support_simple_spinner_dropdown_item,
                category);
        categrory_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        category_spinner.setAdapter(categrory_adapter);

        ArrayAdapter subcategrory_adapter = new ArrayAdapter(AddNewProduct.this,R.layout.support_simple_spinner_dropdown_item,
                subcategory);
        subcategrory_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sub_category_spinner.setAdapter(subcategrory_adapter);

        ArrayAdapter types_clothes_adapter = new ArrayAdapter(AddNewProduct.this,R.layout.support_simple_spinner_dropdown_item,
                types_clothes);
        types_clothes_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type_cloth_spinner.setAdapter(types_clothes_adapter);

        category_spinner.setOnItemSelectedListener(this);
        sub_category_spinner.setOnItemSelectedListener(this);
        type_cloth_spinner.setOnItemSelectedListener(this);

    }

    private void getAllFields() {
        if (!TextUtils.isEmpty(new_product_title.getText().toString())
                && !TextUtils.isEmpty(new_product_description.getText().toString())
                && !TextUtils.isEmpty(new_product_qty.getText().toString())
                && !TextUtils.isEmpty(new_product_price.getText().toString())
                && !TextUtils.isEmpty(new_product_discount.getText().toString())
                && !TextUtils.isEmpty(new_product_material_detail.getText().toString())){
            title = new_product_title.getText().toString().trim();
            description = new_product_description.getText().toString().trim();
            qty = new_product_qty.getText().toString().trim();
            price = new_product_price.getText().toString().trim();
            discount = new_product_discount.getText().toString().trim();
            material_detail = new_product_material_detail.getText().toString().trim();
        }

        if (Final_category.isEmpty()){
            Final_category = category[0];
            Toast.makeText(this, "Category not selected", Toast.LENGTH_SHORT).show();
        }
        if (Final_type_cloth.isEmpty()){
            Final_type_cloth = types_clothes[0];
            Toast.makeText(this, "Cloth not selected", Toast.LENGTH_SHORT).show();
        }
        if (Final_subcategory.isEmpty()){
            Final_subcategory = subcategory[0];
            Toast.makeText(this, "SubCategory not selected", Toast.LENGTH_SHORT).show();
        }

        if (xs_checkbox.isChecked()){
            checkbox_array.add("xs_checkbox");
        }
        if (s_checkbox.isChecked()){
            checkbox_array.add("s_checkbox");
        }
        if (m_checkbox.isChecked()){
            checkbox_array.add("m_checkbox");
        }
        if (l_checkbox.isChecked()){
            checkbox_array.add("l_checkbox");
        }
        if (xl_checkbox.isChecked()){
            checkbox_array.add("xl_checkbox");
        }
        if (xxl_checkbox.isChecked()){
            checkbox_array.add("xxl_checkbox");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 13){
            if (resultCode == RESULT_OK){
                assert data != null;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddNewProduct.this,
                        LinearLayoutManager.HORIZONTAL,false);

                images_recycler.setLayoutManager(linearLayoutManager);

                if (data.getClipData() !=null){
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        uris.add(uri);
                    }
                    Log.d("AddnewProduct", "onActivityResult: Multiple Images"+ uris);
//                    Toast.makeText(this, "Multiple Images", Toast.LENGTH_SHORT).show();
                    addPhoto.setVisibility(View.INVISIBLE);

                    ImageAdapter imageAdapter = new ImageAdapter(uris,AddNewProduct.this);
                    images_recycler.setAdapter(imageAdapter);
                    Log.d("AddnewProduct", "onActivityResult: "+ "succesfull recycler");
                }
                else if (data.getData()!= null){
                    Uri image_path = data.getData();
                    Log.d("AddnewProduct", "onActivityResult: Image"+ image_path);
//                    Toast.makeText(this, "Single Images", Toast.LENGTH_SHORT).show();
                    uris.add(image_path);
                    ImageAdapter imageAdapter = new ImageAdapter(uris,AddNewProduct.this);
                    images_recycler.setAdapter(imageAdapter);
                    addPhoto.setEnabled(false);
                }
                else
                    Toast.makeText(this, "error Activity Result", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.category_spinner:
                 Final_category = this.category[position];
                break;
            case R.id.sub_category_spinner:
                Final_subcategory = this.subcategory[position];
                break;
            case R.id.type_cloth_spinner:
                Final_type_cloth = this.types_clothes[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Final_category = category[0];
        Final_subcategory = subcategory[0];
        Final_type_cloth = types_clothes[0];
    }
}
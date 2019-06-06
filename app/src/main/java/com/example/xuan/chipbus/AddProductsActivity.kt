package com.example.xuan.chipbus

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.example.xuan.chipbus.Class.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_products.*
import java.util.*

class  AddProductsActivity : AppCompatActivity() {

    //camera
    private val PERMISSION_CODE = 1000;
    var photoUri: Uri? = null
    private val IMAGE_CAPTURE_CODE = 1001;
    private val REQUEST_IMAGE_CAPTURE = 1;
   // private var mRtcEngine: RtcEngine? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        add_product_activity_btn.setOnClickListener {
         uploadImageToFirebaseStorage()
            //pushDataToDatabase()
        }

        image_add_product.setOnClickListener {
            Log.d("AddProductActivity","Try to show photo ")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        // permission for use camera
        camera_btn.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if(checkSelfPermission(android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED ) {
                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission,PERMISSION_CODE)
                }
                else {
                    openCamera()
                }
            else {
                openCamera()
            }
        }

    }

    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, " New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, " From the Camera ")
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , photoUri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)
    }
    // Access to library
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data.extras.get("data") as Bitmap
//            imageView.setImageBitmap(imageBitmap)
//        }
//

    // camera
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(this,"Permission dined", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // library
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("AddProductActivity", "Photo was selected")

            photoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            image_add_product.setBackgroundDrawable(bitmapDrawable)
        }
        //return pictures to add product activity
        if(resultCode == Activity.RESULT_OK) {
            image_add_product.setImageURI(photoUri)
            // photoUri, if u check camera on your phone but can't use it, let's change it become selectedPhotoUri
        }


        // access to library image
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            image_add_product.setImageBitmap(imageBitmap)
        }


    }
    private fun uploadImageToFirebaseStorage() {
            if (photoUri == null) return

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(photoUri!!)
                    .addOnSuccessListener {
                        Log.d("AddProductActivity", "Sucessfully uploaded image:${it.metadata?.path}")

                        ref.downloadUrl.addOnSuccessListener {
                            Log.d("AddProductActivity", "File location:$it")

                            //saveImageToFirebaseDatabase(it.toString())
                            pushDataToDatabase(it.toString())
                        }
                    }
                    .addOnFailureListener {

                    }
    }

    /*private fun saveImageToFirebaseDatabase(profileImageProductURL: String) {

            val uid = FirebaseAuth.getInstance().uid ?: ""
            Log.d("AddProductActivity","uid")
            val ref = FirebaseDatabase.getInstance().getReference("/products/$uid")

            val products = Products(uid,name_product_text_view.text.toString(),product_information.text.toString(),product_price.text.toString(),profileImageProductURL )

            ref.setValue(products)
                    .addOnSuccessListener {
                        Log.d("AddProductActivity","Success save data to Firebase")

                    }
                    .addOnFailureListener{
                        Log.d("AddProductActivity","Failed to set value to database")
                    }
    }*/
    //pushing data to database
    private fun pushDataToDatabase (profileImageProductURL: String) {

        //declare
        val productID = FirebaseAuth.getInstance().uid
        val productName = name_product_text_view.text.toString()
        val informationProduct = product_information.text.toString()
        val productPrice = product_price.text.toString()

        //checking the image
        if (photoUri == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/product-data").push()
        val product = Products(productID!!,productName,informationProduct,productPrice,profileImageProductURL)

        //push data to database
        reference.setValue(product)
                .addOnSuccessListener {
                    Log.d("AddProductsActivity","Successfully to push data to database: ${reference.key}")
                }

    }





}



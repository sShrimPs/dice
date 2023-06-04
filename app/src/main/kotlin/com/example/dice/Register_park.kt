package com.example.dice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.dice.kakao.KakaoAddress
import com.example.dice.retrofit.sendImg
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registerpark.*
import kotlinx.android.synthetic.main.activity_sub5.edit_price
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Register_park : AppCompatActivity() {
    lateinit var fulladd: String
    lateinit var postcodes: String
    var ids = intent?.getStringExtra("ids") ?: ""

    var backKeyPressedTime : Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerpark)
        postcode.text = "주소"
        edit_addr1.text = "우편번호"
        search_addr.setOnClickListener {
            val intent = Intent(this, KakaoAddress::class.java)
            startActivity(intent)
        }

        img_btn.setOnClickListener {
            openGallery()
        }
        retrunbtn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()
        postcode.text = "주소"
        edit_addr1.text = "우편번호"
    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("processDATA", Context.MODE_PRIVATE)
        fulladd = sharedPreferences.getString("roadAddress", "").toString()
        postcodes = sharedPreferences.getString("postcode", "").toString()
        postcode.text = postcodes
        edit_addr1.text = fulladd
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            val selectedImages: MutableList<Bitmap> = mutableListOf()

            val clipData = data?.clipData
            if (clipData != null) {
                // 선택한 이미지가 여러 개인 경우
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    val bitmap = uriToBitmap(imageUri)
                    if (bitmap != null) {
                        selectedImages.add(bitmap)
                        image.setImageBitmap(bitmap)
                    }
                }
            } else {
                // 선택한 이미지가 하나인 경우
                val imageUri = data?.data
                val bitmap = uriToBitmap(imageUri)
                if (bitmap != null) {
                    selectedImages.add(bitmap)
                    image.setImageBitmap(bitmap)
                }
            }

            var gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val apiService = retrofit.create(sendImg::class.java)
            var addrs = edit_addr.text.toString()
            // 선택한 이미지들을 사용하여 업로드 작업 수행
            for (bitmap in selectedImages) {
                // 비트맵을 파일로 변환
                val file = createTempFile()
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // 파일을 RequestBody로 변환
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                // 이미지 업로드 API 호출
                val call = apiService.sendImage(fulladd, addrs, postcodes, ids, imagePart)
                Log.d("전송중", fulladd +"\n" + addrs +"\n"+ postcodes +"\n"+ ids +"\n"+ imagePart)
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("Response 완료", response.body().toString())
                        Toast.makeText(applicationContext, "전송 완료", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("Response 실패", t.message.toString())
                        Toast.makeText(applicationContext, "전송 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun uriToBitmap(uri: Uri?): Bitmap? {
        uri?.let {
            val inputStream = contentResolver.openInputStream(uri)
            return BitmapFactory.decodeStream(inputStream)
        }
        return null
    }

    private fun createTempFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${timeStamp}_",  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }


}


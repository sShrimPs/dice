package com.example.dice

import com.example.dice.datamodel.DataModel
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.NonNull
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import android.view.View
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.dice.datamodel.reservers
import com.example.dice.retrofit.reserverService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sub1.*
import kotlinx.android.synthetic.main.popup_layout.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubActivity1 : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var popupView: View
    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: reserverService
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()
    private val marker1 = Marker()
    private val marker2 = Marker()
    private val marker3 = Marker()
    private val infoWindow1 = InfoWindow()
    private val infoWindow2 = InfoWindow()
    private val infoWindow3 = InfoWindow()
    private val addressmark1 = "울산광역시 남구 대학로93 전기컴퓨터공학관"
    private val addressmark2 = "울산광역시 남구 대학로93 울산대학교 체육관"
    private val addressmark3 = "울산광역시 남구 대학로93 KCC생활관"
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    var milesmony: String = ""
    val TAG: String = "Price"
    var isExistBlank = false
    var money: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        setRetrofit()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub1)
        val clipboard=getSystemService(CLIPBOARD_SERVICE) as ClipboardManager


        mainbtn_1.setOnClickListener {
            finish()
        }

        addbtn_1.setOnClickListener{
            val clip: ClipData = ClipData.newPlainText("simple text", addressmark1)
            clipboard.setPrimaryClip(clip)
        }
        addbtn_2.setOnClickListener{
            val clip: ClipData = ClipData.newPlainText("simple text", addressmark2)
            clipboard.setPrimaryClip(clip)
        }
        addbtn_3.setOnClickListener{
            val clip: ClipData = ClipData.newPlainText("simple text",  addressmark3)
            clipboard.setPrimaryClip(clip)
        }
        reserve_1.setOnClickListener {
            showFullScreenPopupDialog()
        }



        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

    }

    private fun callTodoList() {
        mCallTodoList = mRetrofitAPI.getReservers()
        mCallTodoList.enqueue(mRetrofitCallback)
    }
    private fun showFullScreenPopupDialog() {
        // Get the display metrics of the device
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        // Create the PopupWindow object
        val popupWindow = PopupWindow(this)

        // Set the width and height of the PopupWindow object to match the device's screen size
        popupWindow.width = displayMetrics.widthPixels
        popupWindow.height = displayMetrics.heightPixels
        popupWindow.isFocusable = true

        // Set the background color of the PopupWindow to an opaque color
        popupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.popup_background_color)))

        // Set the popup layout as the content view of the PopupWindow
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(R.layout.popup_layout, null)
        popupWindow.contentView = popupView
        val returnButton = popupView.findViewById<Button>(R.id.returnbtn1)

        returnButton.setOnClickListener {
            popupWindow.dismiss()
        }
        val returntxt: TextView = popupView.findViewById(R.id.returnRe)
        returntxt.text = "Error\n" + "Please refresh again"

        // Show the PopupWindow as a full-screen dialog
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        callTodoList()
        val reser = popupView.findViewById<Button>(R.id.refresh1)
        reser.setOnClickListener {
            callTodoList()
        }


    }

    private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d(TAG, "에러코드. => ${t.message.toString()}")
            val returntxt = popupView.findViewById<TextView>(R.id.returnRe)
            returntxt.text = "에러\n" + "다시 한번 새로고침을 해주세요"

        }

        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d(TAG, "수신왼료 $result")
            if (result != null) {
                // Response 데이터가 null이 아닌 경우 처리하는 코드 작성

                var mGson = Gson()
                val dataParsed1 = mGson.fromJson(result, DataModel::class.java)
                val dataParsed2 = mGson.fromJson(result, reservers::class.java)
                val test = dataParsed1.Sonic
                val test1 = dataParsed2.reserver
                val returntxt = popupView.findViewById<TextView>(R.id.returnRe)
                returntxt.text = "Dice 주차장 주차 현황\n" + test + "/6\n" + "예약현황\n" + test1

            } else {
                // Response 데이터가 null인 경우 처리하는 코드 작성
                Log.d(TAG, "에러 발생 ")
            }


        }
    })


    override fun onMapReady(@NonNull naverMap: NaverMap) {

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true
        uiSettings.isLocationButtonEnabled = true
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        //ShareActivity에서 받아온 공유주차장 위치데이터에 따른 마커표시
        this.naverMap = naverMap
        // 장소 마커
        marker1.position = LatLng(35.543937, 129.254998)
        marker1.map = naverMap
        marker1.captionTextSize = 16f
        marker1.captionText = "Dice Park 1호점"
        marker1.subCaptionText = "\n(주소를 볼려면 마커를 눌러주세요)"
        marker1.subCaptionColor = Color.BLUE
        marker1.subCaptionHaloColor = Color.rgb(200, 255, 200)
        marker1.subCaptionTextSize = 10f

        marker2.position = LatLng(35.545000, 129.2582)
        marker2.map = naverMap
        marker2.captionTextSize = 16f
        marker2.captionText = "Dice Park 2호점"
        marker2.subCaptionText = "\n(주소를 볼려면 마커를 눌러주세요)"
        marker2.subCaptionColor = Color.BLUE
        marker2.subCaptionHaloColor = Color.rgb(200, 255, 200)
        marker2.subCaptionTextSize = 10f

        marker3.position = LatLng(35.546, 129.2575)
        marker3.map = naverMap
        marker3.captionTextSize = 16f
        marker3.captionText = "Dice Park 3호점"
        marker3.subCaptionText = "\n(주소를 볼려면 마커를 눌러주세요)"
        marker3.subCaptionColor = Color.BLUE
        marker3.subCaptionHaloColor = Color.rgb(200, 255, 200)
        marker3.subCaptionTextSize = 10f



        addbtn_1.visibility = View.INVISIBLE
        addbtn_2.visibility = View.INVISIBLE
        addbtn_3.visibility = View.INVISIBLE


        infoWindow1.adapter = object : InfoWindow.DefaultTextAdapter(application) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return addressmark1  // 정보창 내용 넣기
            }
        }
        infoWindow2.adapter = object : InfoWindow.DefaultTextAdapter(application) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return addressmark2  // 정보창 내용 넣기
            }
        }
        infoWindow3.adapter = object : InfoWindow.DefaultTextAdapter(application) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return addressmark3  // 정보창 내용 넣기
            }
        }

        val listener1 = Overlay.OnClickListener { overlay ->
            val markerA = overlay as Marker

            if (markerA.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶

                infoWindow1.open(marker1)
                infoWindow2.close()
                infoWindow3.close()
                reserve_1.visibility = View.VISIBLE
                addbtn_1.visibility = View.VISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE

            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow1.close()
                reserve_1.visibility = View.INVISIBLE
                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE

            }

            true
        }
        val listener2 = Overlay.OnClickListener { overlay ->
            val markerA = overlay as Marker

            if (markerA.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶

                infoWindow2.open(marker2)
                infoWindow1.close()
                infoWindow3.close()
                reserve_1.visibility = View.INVISIBLE

                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.VISIBLE
                addbtn_3.visibility = View.INVISIBLE


            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow2.close()
                reserve_1.visibility = View.INVISIBLE

                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE


            }

            true
        }

        val listener3 = Overlay.OnClickListener { overlay ->
            val markerA = overlay as Marker

            if (markerA.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶

                infoWindow3.open(marker3)
                infoWindow1.close()
                infoWindow2.close()
                reserve_1.visibility = View.INVISIBLE

                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.VISIBLE

            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow3.close()
                reserve_1.visibility = View.INVISIBLE
                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE

            }

            true
        }


        marker1.onClickListener = listener1
        marker2.onClickListener = listener2
        marker3.onClickListener = listener3

// 정보창 열기
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun setRetrofit(){
        var gson = GsonBuilder().setLenient().create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        mRetrofitAPI = mRetrofit.create(reserverService::class.java)
    }


}
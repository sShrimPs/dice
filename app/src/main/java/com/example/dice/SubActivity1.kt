package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import androidx.annotation.NonNull
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import android.content.ClipboardManager
import android.view.View
import android.content.ClipData
import kotlinx.android.synthetic.main.activity_sub1.*
class SubActivity1 : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val marker1 = Marker()
    private val marker2 = Marker()
    private val marker3 = Marker()
    private val infoWindow1 = InfoWindow()
    private val infoWindow2 = InfoWindow()
    private val infoWindow3 = InfoWindow()
    private val addressmark1 = "울산광역시 남구 대학로93 전기컴퓨터공학관"
    private val addressmark2 = "울산광역시 남구 대학로93 울산대학교 체육관"
    private val addressmark3 = "울산광역시 남구 대학로93 KCC생활관"

    override fun onCreate(savedInstanceState: Bundle?) {
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

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)
    }
    override fun onMapReady(@NonNull naverMap: NaverMap) {

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true
        uiSettings.isLocationButtonEnabled = true
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

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
                addbtn_1.visibility = View.VISIBLE
                addbtn_2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE


            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow1.close()
                addbtn_1.visibility = View.INVISIBLE

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
                addbtn_2.visibility = View.VISIBLE
                addbtn_1.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE


            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow2.close()
                addbtn_2.visibility = View.INVISIBLE


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
                addbtn_3.visibility = View.VISIBLE
                addbtn_1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE


            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow3.close()
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
}
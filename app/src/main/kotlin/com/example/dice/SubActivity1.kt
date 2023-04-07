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
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_sub1.*

class SubActivity1 : AppCompatActivity(), OnMapReadyCallback {
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

    private val A1 = 1
    private val A2 = 0
    private val A3 = 1
    private val A4 = 1
    private val A5 = 1
    private val A6 = 1

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
        //아래 A1~A6는 주차구역의 버튼 확성 비활성화 및 가배정 기능
        if (A1 == 1) {
            A1spot.isEnabled = true
            A1spot.setBackgroundResource(R.drawable.green_circle_button)
            A1spot.setOnClickListener{
//가배정 버튼 -> A1버튼 터치 -> 가배정 하시겠습니까??? - 만약 30분내 입차하지 않을시 위약금 10000원이 청구됩니다.
                //경고문 필요 1. 반드시 30분 내에 주차장에 도착해주세요. 2. 입차확인은 번호판을 통해 자동으로 확인됩니다.
                //
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A1 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A1 == 0) {
            A1spot.isEnabled = false
            A1spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }
        if (A2 == 1) {
            A2spot.isEnabled = true
            A2spot.setBackgroundResource(R.drawable.green_circle_button)
            A2spot.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A2 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A2 == 0) {
            A2spot.isEnabled = false
            A2spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }
        if (A3 == 1) {
            A3spot.isEnabled = true
            A3spot.setBackgroundResource(R.drawable.green_circle_button)
            A3spot.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A3 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A3 == 0) {
            A3spot.isEnabled = false
            A3spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }
        if (A4 == 1) {
            A4spot.isEnabled = true
            A4spot.setBackgroundResource(R.drawable.green_circle_button)
            A4spot.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A4 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A4 == 0) {
            A4spot.isEnabled = false
            A4spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }
        if (A5 == 1) {
            A5spot.isEnabled = true
            A5spot.setBackgroundResource(R.drawable.green_circle_button)
            A5spot.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A5 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A5 == 0) {
            A5spot.isEnabled = false
            A5spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }
        if (A6 == 1) {
            A6spot.isEnabled = true
            A6spot.setBackgroundResource(R.drawable.green_circle_button)
            A6spot.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("주의사항")
                builder.setMessage("A6 가배정 하시겠습니까?\n1. 반드시 30분 이내에 입차해주세요\n시간내 미입차시 위약금 10000원이 청구됩니다.\n2. 입차확인은 번호판을 통해 자동으로 확인됩니다.\n3. 이재호 개병신")
                builder.setPositiveButton("네") { dialog, which ->
                    // 여기에 기능 추가 필요 - 가배정 이후 동작
                    Toast.makeText(this, "가배정 되었습니다!", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(this, "취소하였습니다..", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else if (A6 == 0) {
            A6spot.isEnabled = false
            A6spot.setBackgroundResource(R.drawable.red_circle_button)
        }
        else{
            print("error!!")
        }

//아래 parkspot은 주차장 클릭후 가배정 버튼을 의미
        parkspot1.setOnClickListener {
            parkspotclose.visibility = View.VISIBLE
            park_image.visibility=View.VISIBLE
            A1spot.visibility=View.VISIBLE
            A2spot.visibility=View.VISIBLE
            A3spot.visibility=View.VISIBLE
            A4spot.visibility=View.VISIBLE
            A5spot.visibility=View.VISIBLE
            A6spot.visibility=View.VISIBLE
        }
        parkspotclose.setOnClickListener {
            park_image.visibility=View.GONE
            parkspotclose.visibility = View.GONE
            A1spot.visibility=View.GONE
            A2spot.visibility=View.GONE
            A3spot.visibility=View.GONE
            A4spot.visibility=View.GONE
            A5spot.visibility=View.GONE
            A6spot.visibility=View.GONE
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

    private fun cancelMarkerByLongitude(longitude: Double) {
        // Find the marker with the given longitude from the markers list
        val markerToRemove = markers.find { it.position.longitude == longitude }
        // If a marker with the given longitude exists, remove it from the map and the markers list
        markerToRemove?.apply {
            map = null
            markers.remove(this)
        }
    }

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
                addbtn_1.visibility = View.VISIBLE
                parkspot1.visibility = View.VISIBLE
                addbtn_2.visibility = View.INVISIBLE
                parkspot2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE
                parkspot3.visibility = View.INVISIBLE

            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow1.close()
                addbtn_1.visibility = View.INVISIBLE
                parkspot1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                parkspot2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE
                parkspot3.visibility = View.INVISIBLE

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
                addbtn_1.visibility = View.INVISIBLE
                parkspot1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.VISIBLE
                parkspot2.visibility = View.VISIBLE
                addbtn_3.visibility = View.INVISIBLE
                parkspot3.visibility = View.INVISIBLE


            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow2.close()
                addbtn_1.visibility = View.INVISIBLE
                parkspot1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                parkspot2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE
                parkspot3.visibility = View.INVISIBLE


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
                addbtn_1.visibility = View.INVISIBLE
                parkspot1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                parkspot2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.VISIBLE
                parkspot3.visibility = View.VISIBLE

            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow3.close()
                addbtn_1.visibility = View.INVISIBLE
                parkspot1.visibility = View.INVISIBLE
                addbtn_2.visibility = View.INVISIBLE
                parkspot2.visibility = View.INVISIBLE
                addbtn_3.visibility = View.INVISIBLE
                parkspot3.visibility = View.INVISIBLE

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
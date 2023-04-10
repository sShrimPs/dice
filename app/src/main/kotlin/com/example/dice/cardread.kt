package com.example.dice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.MifareClassic
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.retrofit.cardService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cardread.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class cardread : AppCompatActivity(), NfcAdapter.ReaderCallback {
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    var ids = intent?.getStringExtra("ids") ?: ""
    private var nfcAdapter: NfcAdapter? = null
    var uid:String = ""
    var uidDecimal:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardread)
        pref = getSharedPreferences("id", Context.MODE_PRIVATE)
        preid = pref.edit()
        var saveid = pref.getString("InputData","")
        ids = saveid.toString()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this) ?: run {
            Toast.makeText(this, "이 장치는 nfc를 지원하지 않습니다.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(this, "NFC가 활성화 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            enableNfcSetting()
        } else {
            Toast.makeText(this, "NFC가 활성화 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        returnbtn.setOnClickListener {
            val intents = Intent(this, SubActivity5::class.java)
            intent.apply {
                this.putExtra("id", ids)
                this.putExtra("uid", uid)
            }
            startActivity(intents)
        }
    }

    private fun enableNfcSetting() {
        // NFC 설정 화면으로 이동하는 인텐트 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startActivity(Intent(Settings.Panel.ACTION_NFC))
        } else {
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.let {
            val techList = arrayOf(arrayOf(MifareClassic::class.java.name))
            it.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V, null)
        }
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        try {
            val mifareClassic = MifareClassic.get(tag)
            mifareClassic?.let {
                if (!it.isConnected) {
                    it.connect()
                }
                val sectorCount = it.sectorCount
                for (i in 0 until sectorCount) {
                    val auth = it.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)
                    if (auth) {
                        val blockCount = it.getBlockCountInSector(i)
                        for (j in 0 until blockCount) {
                            val blockIndex = it.sectorToBlock(i) + j
                            val data = it.readBlock(blockIndex)
                            Log.d("결과", "Sector $i, Block $j, Data: ${bytesToHexString(data)}")
                        }
                    }
                }
                uid = bytesToHexString(it.tag.id)
                var uiddem = java.lang.Long.parseLong(uid, 16)
                uidDecimal = uiddem.toString()
                it.close()
                val jsonObject = JsonObject().apply {
                    addProperty("id", ids)
                    addProperty("uid", uidDecimal)
                }
                Log.d("Flask Sever", "카드정보 전송중 $jsonObject")
                sendDataToServer(jsonObject)

                runOnUiThread {
                    Toast.makeText(this, "UID: $uid", Toast.LENGTH_LONG).show()
                    taguidTextView.setText(uid)
                    Log.d("uid", "UID IS $uid")
                }
            } ?: run {
                Log.d("결과", "Tag is null")
                runOnUiThread {
                    Toast.makeText(this@cardread, "카드를 다시 접촉해주세요.", Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: TagLostException) {
            runOnUiThread {
                Toast.makeText(this@cardread, "카드를 다시 접촉해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bytesToHexString(bytes: ByteArray?): String {
        if (bytes == null) {
            return ""
        }
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    private fun sendDataToServer(jsonObject: JsonObject) {

        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(cardService::class.java)

        val call = apiService.cardSever(jsonObject)
        Log.d("Retrofit sever", "전송중")

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.d("Response 완료", response.body().toString())

                } else {
                    Log.d("Response 완료", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("전송 실패", t.message.toString())
            }
        }
        )
        Log.d("Retrofit sever", "전송완료")
    }

}
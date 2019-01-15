package com.example.shakeib.salah

import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Room
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shakeib.salah.data.AppDatabase
import com.example.shakeib.salah.data.SalahTable
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity()
    , Callback<SalahResponse> {
    var salahTimings: ArrayList<SalahResponse.Data> = arrayListOf()
    lateinit var db: AppDatabase
    lateinit var date: String
    var salahTableList: ArrayList<SalahTable> = arrayListOf()
    var year = 0
    var month = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //make button 1 visbility false later change this to image or something
        noConnection.visibility = View.GONE
        noConnectionTv.visibility = View.GONE
        linearLayout.visibility = View.GONE
        locationTv.visibility = View.GONE

        //making database object and making table and database if not exist
        db = Room.databaseBuilder(this, AppDatabase::class.java, "Salah").allowMainThreadQueries().build()

        //gettting current date of the mobile
        val dateObject = Date()

        //Getting year and month from current date
        val calendar = Calendar.getInstance()
        calendar.time = dateObject
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)

        //Parsing date into dd-MM-yyyy format
        date = SimpleDateFormat("dd-MM-yyyy").format(dateObject)
        dateTv.setText(date)
        //to get the data of the current date
       // var listTemp = db.salahDao().selectAll(date)
        val listTemp = getData()
        if (listTemp.isEmpty()) {
            makeApiRequest()
        }else{
            fajrTime.setText(listTemp[0].fajr)
            sunriseTime.setText(listTemp[0].sunrise)
            dhuhrTime.setText(listTemp[0].dhuhr)
            asrTime.setText(listTemp[0].asr)
            magribTime.setText(listTemp[0].magrib)
            ishaTime.setText(listTemp[0].isha)
            linearLayout.visibility = View.VISIBLE
            locationTv.visibility = View.VISIBLE
        }



        noConnection.setOnClickListener {
            makeApiRequest()

        }

    }

    override fun onFailure(call: Call<SalahResponse>, t: Throwable) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<SalahResponse>, response: Response<SalahResponse>) {
        salahTimings.clear()
        salahTableList.clear()
        val timings = response.body()
        Toast.makeText(this@MainActivity, "DONE", Toast.LENGTH_SHORT).show()
        timings?.also { vol ->
            vol.data.also {
                salahTimings.addAll(it.toList())

            }

            for (objects in salahTimings) {
                var salahTable = SalahTable(
                    0,
                    changeTime(objects.timings.Fajr),
                    changeTime(objects.timings.Sunrise),
                    changeTime(objects.timings.Dhuhr),
                    changeTime(objects.timings.Asr),
                    changeTime(objects.timings.Maghrib),
                    changeTime(objects.timings.Isha),
                    objects.date.gregorian.date
                )
                salahTableList.add(salahTable)

            }
            db.salahDao().insertAll(
                salahTableList
            )

            //again fetching because avoid making algo complex
            val listTemp = getData()

            //setting up views after fetching data
            fajrTime.setText(listTemp[0].fajr)
            sunriseTime.setText(listTemp[0].sunrise)
            dhuhrTime.setText(listTemp[0].dhuhr)
            asrTime.setText(listTemp[0].asr)
            magribTime.setText(listTemp[0].magrib)
            ishaTime.setText(listTemp[0].isha)

            noConnection.visibility = View.GONE
            noConnectionTv.visibility= View.GONE
            linearLayout.visibility = View.VISIBLE
            locationTv.visibility = View.VISIBLE


        }

    }

    private fun getData():List<SalahTable>{
        var listTemp = db.salahDao().selectAll(date)
        return listTemp
    }

    private fun makeApiRequest() {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            Api().service.getTimings("Karachi", "Pakistan", 1, year, month + 1).enqueue(this)
        } else {
            noConnection.visibility = View.VISIBLE
            noConnectionTv.visibility = View.VISIBLE
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show()
        }

    }

    private fun changeTime(time:String):String{
        var time = time
        time = time.split(" ")[0]
        var sdf = SimpleDateFormat("HH:mm")
        var date = sdf.parse(time)
        return SimpleDateFormat("h:mm a").format(date)

    }


}

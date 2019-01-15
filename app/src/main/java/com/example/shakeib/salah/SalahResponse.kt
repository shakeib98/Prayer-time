package com.example.shakeib.salah

import retrofit2.http.Field
import java.io.Serializable

class SalahResponse : Serializable {
    var data: List<Data> = ArrayList()

    class Data {
        var timings: Timings = Timings()
        var date: Date = Date()

        class Timings {
            var Fajr: String = ""
            var Sunrise: String = ""
            var Dhuhr: String = ""
            var Asr: String = ""
            var Maghrib: String = ""
            var Isha: String = ""
        }

        class Date {
            var gregorian:Georgian = Georgian()

            class Georgian{
                var date:String = ""
            }
        }
    }
}
package com.rmaproject.myqoran

import android.os.FileUriExposedException

//Nomor 3
open class HybridCar(){
    var runningmode:Boolean = false
    fun switchMode(){
        print("Bertukar!")
    }
}

class PetrolCar(): HybridCar(){
    var fuelLevel:Int = 0
    val tankCapacity:Int = 0

    fun fillUp (Nambah:Int): String {
        fuelLevel = fuelLevel + Nambah
        fuelLevel = fuelLevel + Nambah
        if (fuelLevel > tankCapacity) {
            return "Batre kepenuhan! ${fuelLevel}/${tankCapacity}, kurangi"
        } else if (fuelLevel == tankCapacity) {
            return "Batre sudah penuh @100%"
        } else {
            return "Bensin diisi: $fuelLevel"
        }

    }
}

class ElectricCar(): HybridCar(){
    var batteryLevel:Int = 0
    var batteryCapacity:Int = 0

    fun recharge (Nambah:Int): String {

        batteryLevel = batteryLevel + Nambah
        if (batteryLevel > batteryCapacity) {
            return "Batre kepenuhan! ${batteryLevel}/${batteryCapacity}, kurangi"
        } else if (batteryLevel == batteryCapacity) {
            return "Batre sudah penuh @100%"
        } else {
            return "Nambahin: $batteryLevel"
        }
    }
}

//Nomor 4
open class Person(){
    val name:String = ""
    val age:Int = 0
    val size:Int = 0
    val hairColor:String = ""

    fun Walk(Distance:Int):String {
        return "Dia telah berjalan sejauh $Distance km"
    }
    fun Eat (Food:String):String {
        return "Dia lagi makan $Food"
    }
    fun Speak (Language:String):String {
        return "Dia berbahasa $Language"
    }
}

class Student(): Person(){
    fun Study(Subject:String):String{
        return "Belajar: $Subject"
    }
}

class Teacher():Person(){
    fun Teach(SubjectGuru:String):String{
        return "Mengajar $SubjectGuru"
    }
}

open class Employee(){
    val name:String = ""
    val id:Int = 0
    var phone:String = ""
    fun updatePhone(nomorBaru:String):Boolean{
        if (nomorBaru.isNotEmpty()){
            phone = nomorBaru
            return true
        } else {
            return false
        }
    }
    fun getById(IdYangDicari:Int):String{
        return "ID yang dicari adalah $IdYangDicari"
    }
}
class Designer(){
    var DribbleLink:String = ""
    
    fun UpdateDribbleLink(DribbleLinkBaru:String):String{
        if (DribbleLinkBaru.isNotEmpty()){
            DribbleLink = DribbleLinkBaru
            return "Dribblelink baru: $DribbleLinkBaru"
        } else{
            return "G ada dribble Link baru LOL"
        }
    }
}



fun main () {
    val Mamang = ElectricCar()
    Mamang.batteryLevel = 99
    Mamang.batteryCapacity = 100

    println("${Mamang.recharge(2)}")
}
package com.rmaproject.myqoran

open class Item(){
    var name:String = ""
    var description:String = ""
    var price:Int = 0
    fun viewDescription(adaDeskripsi:Boolean):String{
        return "Deskripsi nih boss"
    }
    fun addtoShoppingBasket(adadiShopBasket:Boolean):String {
        return "Ada ngab"
    }
    fun removefromshoppingBasket(dikeluarin:Boolean):String {
        return "Dikeluarkan"
    }
}

class MP3 : Item(){
    var artist:String = ""
    var duration:Int = 0
    fun play(dinyalakan:Boolean):String {
        return "Nyala"
    }

    fun download(didownload:Boolean):String {
        return "Downloaded"
    }
}
class DVD : Item(){
    var certificate:String = ""
    var duration:Int = 0
    var actors:String = ""
    fun viewTrailer(diputar:Boolean):String {
        return "Diputar"
    }
}
class Book : Item(){
    var author:String = ""
    var numberofPages:Int = 0
    var genre:String = ""
    fun previewContent(dibuka:Boolean):String {
        return "Dibuka"
    }
}

fun Inheritance(){
    val Murottal = MP3()
    Murottal.name = "Surat Al-Baqarah"
    Murottal.description = "Bacaan surat Al-Baqarah"
    Murottal.price = 200000
    Murottal.artist = "Insert nama Syeikh disini"
    Murottal.duration = 2

    val KasetFilm = DVD()
    KasetFilm.name = "Mamangku Di Atas Kuda"
    KasetFilm.description = "Menceritakan seorang Mamang yang hobinya naik diatas kuda"
    KasetFilm.price = 200000
    KasetFilm.certificate = "Mantap"
    KasetFilm.actors = "Mamang"
    KasetFilm.duration = 2

    val Novel = Book()
    Novel.name = "Babang Sopo"
    Novel.description = "Menceritakan seorang Babang yang hobinya bertanya 'yang sabar ya bos' "
    Novel.price = 200000
    Novel.author = "Raka M.A"
    Novel.numberofPages = 23
    Novel.genre = "Comedy"

    println("MP3:")
    println(Murottal.name)
    println("Apakah nyala? ${Murottal.play(true)}")
    println("===========================")

    println("DVD:")
    println(KasetFilm.name)
    println("Apakah trailer sedang diputar? ${KasetFilm.viewTrailer(true)}")
    println("===========================")

    println("Book:")
    println(Novel.name)
    println("Apakah bukunya terbuka? ${Novel.previewContent(true)}")
    println("===========================")
}

fun main() {



}

fun ForLoop() {
    println("Nama: Raka Muhammad Al-Hafidz")
    println("Kelas: XI RPL")
    println("Mapel: PBO XI RPL")
    println("----------------------------------------")
    println("Surah Al-Fiil dengan Array")
    println("بِسْمِ اللّهِ الرَّحْم نِ الرَّحِيْمِ")

    val SurahAlFil: Array<String> = arrayOf(
        "اَلَمْ تَرَ كَيْفَ فَعَلَ رَبُّكَ بِاَصْحٰبِ الْفِيْل",
        "اَلَمْ يَجْعَلْ كَيْدَهُمْ فِيْ تَضْلِيْل",
        "وَّاَرْسَلَ عَلَيْهِمْ طَيْرًا اَبَابِيْل",
        "تَرْمِيْهِمْ بِحِجَارَةٍ مِّنْ سِجِّيْل",
        "فَجَعَلَهُمْ كَعَصْفٍ مَّأْكُوْلٍ"
    )
    //Untuk memotong sebuah value dalam Array, gunakan array.slice

    var index:Int = 0

    for (ayah in SurahAlFil){
//        println(ayah)
        index = index + 1

        var GanjilGenap: String = ""

        if (index % 2 == 0){
            GanjilGenap = "Genap"
        } else {
            GanjilGenap = "Ganjil"
        }

        println("$index. $GanjilGenap $ayah")
    }

//    SurahAlFil.forEachIndexed { index, s ->
//        val OddorEven:Int = index
//        val number:Int = index + 1
//
//    }
    println("----------------------------------------")

}
package com.example.biblioteca.models

data class libro(
   var idLibro: String,
   var titulo: String,
   var autor : String,
   var isbn : String,
   var genero : String,
   var numEjemplarDisponible : Int,
   var numEjemplarOcupado: Int,
)

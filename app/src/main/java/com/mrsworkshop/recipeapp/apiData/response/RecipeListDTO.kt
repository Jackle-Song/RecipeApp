package com.mrsworkshop.recipeapp.apiData.response

import com.google.gson.JsonElement

data class RecipeListDTO (
    var s : Int? = null,
    var d : MutableList<RecipeListData>? = mutableListOf(),
    var t : Int? = null,
    var p : RecipeListCountData? = RecipeListCountData()
) : DTO()

data class RecipeListData (
    var id : String? = null,
    var Title : String? = null,
    var Ingredients : JsonElement? = null,
    var Instructions : String? = null,
    var Image : String? = null,
)

data class RecipeListCountData (
    var limitstart : Int? = null,
    var limit : Int? = null,
    var total : Int? = null,
    var pagesStart : Int? = null,
    var pagesStop : Int? = null,
    var pagesCurrent : Int? = null,
    var pagesTotal : Int? = null,
)
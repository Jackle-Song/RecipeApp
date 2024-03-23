package com.mrsworkshop.recipeapp.apiData.response

abstract class DTO (
    var responseCode: Int? = null,
    var responseMessage: String? = null,

    var errorKey: String? = null,
    var statusCode: Int? = null,
    var httpDetails: String? = null
)
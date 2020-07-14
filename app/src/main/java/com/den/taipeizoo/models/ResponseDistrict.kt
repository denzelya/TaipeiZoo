package com.den.taipeizoo.models

data class ResponseDistrict(
    val result: ResultDistrict
)

data class ResultDistrict(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<District>,
    val sort: String
)

data class District(
    val E_Category: String,
    val E_Geo: String,
    val E_Info: String,
    val E_Memo: String,
    val E_Name: String,
    val E_Pic_URL: String,
    val E_URL: String,
    val E_no: String,
    val _id: Int
)
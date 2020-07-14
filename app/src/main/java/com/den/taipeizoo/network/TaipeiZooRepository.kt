package com.den.taipeizoo.network

class TaipeiZooRepository(
    private val api: TaipeiZooApi
) : SafeApiRequest() {
    suspend fun getDistrict() = apiRequest { api.getDistricts() }

    suspend fun getPlant(location: String? = null) =
        apiRequest { api.getPlants(q = location ?: "") }
}
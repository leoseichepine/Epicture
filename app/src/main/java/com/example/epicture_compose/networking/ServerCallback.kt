package com.example.epicture_compose.networking

import org.json.JSONObject

public interface ServerCallback
{
    fun onSuccess(result: JSONObject)
}
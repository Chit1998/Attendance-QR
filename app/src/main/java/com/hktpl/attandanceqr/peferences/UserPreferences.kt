package com.hktpl.attandanceqr.peferences

import android.content.Context
import android.content.SharedPreferences
import com.hktpl.attandanceqr.objects.TAG.EMP_ID
import com.hktpl.attandanceqr.objects.TAG.LOCATION_STATUS
import com.hktpl.attandanceqr.objects.TAG.OID
import com.hktpl.attandanceqr.objects.TAG.USER_FILE_NAME
import com.hktpl.attandanceqr.objects.TAG.NAME
import com.hktpl.attandanceqr.objects.TAG.PHONE
import com.hktpl.attandanceqr.objects.TAG.SITE_OID

class UserPreferences(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(USER_FILE_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setData(empId: String?, name: String?, phone: String?){
        editor.putString(EMP_ID,empId)
        editor.putString(NAME,name)
        editor.putString(PHONE,phone)
        editor.commit()
    }

    fun getEmpId(): String? {
        return preferences.getString(EMP_ID, null)
    }

    fun getSiteOid(): String? {
        return preferences.getString(SITE_OID, null)
    }

    fun getName(): String? {
        return preferences.getString(NAME, null)
    }

    fun getPhone(): String? {
        return preferences.getString(PHONE, null)
    }

    fun getLocationStatus(): Boolean {
        return preferences.getBoolean(LOCATION_STATUS, false)
    }

    fun setOid(oid: String){
        editor.putString(OID,oid)
        editor.commit()
    }

    fun setSiteOid(siteOid: String){
        editor.putString(SITE_OID,siteOid)
        editor.commit()
    }

    fun setLocationStatus(locationStatus: Boolean){
        editor.putBoolean(LOCATION_STATUS,locationStatus)
        editor.commit()
    }

    fun getOid(): String? {
        return preferences.getString(OID, null)
    }


    fun clear(): Unit {
        editor.clear()
        editor.commit()
    }
}

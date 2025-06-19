package com.hktpl.attandanceqr.peferences

import android.content.Context
import android.content.SharedPreferences
import com.hktpl.attandanceqr.objects.TAG.EMP_ID
import com.hktpl.attandanceqr.objects.TAG.OID
import com.hktpl.attandanceqr.objects.TAG.USER_FILE_NAME
import com.hktpl.attandanceqr.objects.TAG.NAME
import com.hktpl.attandanceqr.objects.TAG.PHONE

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

    fun getName(): String? {
        return preferences.getString(NAME, null)
    }

    fun getPhone(): String? {
        return preferences.getString(PHONE, null)
    }


    fun setOid(oid: String){
        editor.putString(OID,oid)
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

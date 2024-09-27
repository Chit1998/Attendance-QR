package com.hktpl.attandanceqr.peferences

import android.content.Context
import android.content.SharedPreferences
import com.hktpl.attandanceqr.objects.TAG.OID
import com.hktpl.attandanceqr.objects.TAG.ROLE_STATUS
import com.hktpl.attandanceqr.objects.TAG.USER_FILE_NAME
import com.hktpl.attandanceqr.objects.TAG.USER_ID
import com.hktpl.attandanceqr.objects.TAG.USER_NAME

class UserPreferences(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(USER_FILE_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setRole(role: String) {
        editor.putString(ROLE_STATUS, role)
        editor.commit()
    }

    fun getRole(): String? {
        return preferences.getString(ROLE_STATUS,null)
    }

    fun setData(userid: String?, username: String?){
        editor.putString(USER_ID,userid)
        editor.putString(USER_NAME,username)
        editor.commit()
    }

    fun getUserId(): String? {
        return preferences.getString(USER_ID, null)
    }

    fun getName(): String? {
        return preferences.getString(USER_NAME, null)
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

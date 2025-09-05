package com.hktpl.attandanceqr.objects

object TAG {
    init {
        System.loadLibrary("apikey")
    }
    external fun getApi() : String
    external fun getApiTest() : String

    const val FOUND = "USER FOUND"

//    TODO Preferences
    const val USER_FILE_NAME = "user"
    const val EMP_ID = "empId"
    const val NAME = "name"
    const val PHONE = "phone"
    const val OID = "id"
    const val LOCATION_STATUS = "locationStatus"
    const val SITE_OID = "siteOid"


//    permission codes
    const val LOCATION_PERMISSION_CODE = 123
    const val GPS_REQUEST_CODE = 456

}
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := apikey
LOCAL_SRC_FILES := apikey.c

include $(BUILD_SHARED_LIBRARY)
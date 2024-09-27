#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_hktpl_attandanceqr_objects_TAG_getApi(JNIEnv *env, jobject instance) {
    return (*env)-> NewStringUTF(env, "aHR0cHM6Ly9xcmRhLmhrdHBsLmNvbS9xcmRhL3Jlc3QvZW1wLw==");
}
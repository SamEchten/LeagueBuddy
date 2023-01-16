//
// Created by bramj on 16-1-2023.
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_leaguebuddy_api_LeagueApiHelper_00024Keys_lolApiKey(JNIEnv *env, jobject thiz) {
    std::string api_key = "RGAPI-f11bda65-19d0-4823-8b4c-d84d069bbb0d";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_leaguebuddy_api_GameNewsApiHelper_00024Keys_gApiKey(JNIEnv *env, jobject thiz) {
    std::string api_key = "2f1a547abdmsh179a9be92a0054dp1ce29ajsnc669ce37838b";
    return env->NewStringUTF(api_key.c_str());
}
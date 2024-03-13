#include <jni.h>
#include "react-native-fast-video.h"

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_fastvideo_FastVideoModule_nativeMultiply(JNIEnv *env, jclass type, jdouble a, jdouble b) {
    return fastvideo::multiply(a, b);
}

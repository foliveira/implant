/**
 * Copyright (C) 2010 Fábio Oliveira.
 * All rights reserved.
 *
 * This file is part of the IMPLANT project.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Fábio Oliveira nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

/*
 * ARToolkitPlusJNI.cpp
 * author: Fábio Oliveira
 * This is the bridge between the Java and the C++ part for the ARToolkitPlus module.
 */

#include <android/log.h>
#include <ARToolkitPlus.javanative.h>
#include <ARToolKitPlus/TrackerSingleMarker.h>

/*
 * Uncomment the second line to disable debugging messages logging
 */
#define DEBUG_LOG
//#undef DEBUG_LOG

#define LOG_TAG "NativeIMPLANT"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

using namespace ARToolKitPlus;

TrackerSingleMarker* tracker;
jclass exception_rtti;

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    start
 * Signature: (IIIIIII)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setup
  (JNIEnv * env, jclass, jint width, jint height, jint maxPatterns, jint pWidth, jint pHeight, jint pSamples, jint maxLoadPatterns)
{
	if(pWidth != pHeight || (pWidth % pSamples) != 0) {
		exception_rtti = env->FindClass("pt/com/gmv/lab/implant/ar/toolkit/exceptions/ARToolkitException");
		if (exception_rtti != NULL)
			env->ThrowNew(exception_rtti, "failed to setup pattern parameters");
	}

	tracker = new TrackerSingleMarker(width, height, maxPatterns, pWidth, pHeight, pSamples, maxLoadPatterns);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    init
 * Signature: (Ljava/lang/String;FF)Z
 */
JNIEXPORT
jboolean
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_init
  (JNIEnv * env, jclass, jstring calibFile, jfloat nearClip, jfloat farClip)
{
	const char *cparam_name = env->GetStringUTFChars(calibFile, NULL);

	jboolean inited = tracker->init(cparam_name, nearClip, farClip);

	env->ReleaseStringUTFChars(calibFile, cparam_name);

	return inited;
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    changeCameraSize
 * Signature: (II)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_changeCameraSize
  (JNIEnv *, jclass, jint width, jint height)
{
	tracker->changeCameraSize(width, height);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setPixelFormat
 * Signature: (I)Z
 */
JNIEXPORT
jboolean
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setPixelFormat
  (JNIEnv *, jclass, jint format)
{
	return tracker->setPixelFormat(static_cast<PIXEL_FORMAT>(format));
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setLoadUndistortionLUT
 * Signature: (Z)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setLoadUndistortionLUT
  (JNIEnv *, jclass, jboolean enable)
{
	tracker->setLoadUndistLUT(enable);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setPoseEstimator
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setPoseEstimator
  (JNIEnv *, jclass, jint mode)
{
	tracker->setPoseEstimator(static_cast<POSE_ESTIMATOR>(mode));
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    activateVignettingCompensation
 * Signature: (ZIII)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_activateVignettingCompensation
  (JNIEnv *, jclass, jboolean enable, jint corners, jint lr, jint tb)
{
	tracker->activateVignettingCompensation(enable, corners, lr, tb);
}


/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setPatternWidth
 * Signature: (F)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setPatternWidth
  (JNIEnv *, jclass, jfloat value)
{
	tracker->setPatternWidth(value);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setBorderWidth
 * Signature: (F)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setBorderWidth
  (JNIEnv *, jclass, jfloat value)
{
	tracker->setBorderWidth(value);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setThreshold
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setThreshold
  (JNIEnv *, jclass, jint value)
{
	tracker->setThreshold(value);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    getThreshold
 * Signature: ()I
 */
JNIEXPORT
jint
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_getThreshold
  (JNIEnv *, jclass)
{
	return tracker->getThreshold();
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    activateAutoThreshold
 * Signature: (Z)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_activateAutoThreshold
  (JNIEnv *, jclass, jboolean enable)
{
	tracker->activateAutoThreshold(enable);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    isAutoThresholdActivated
 * Signature: ()Z
 */
JNIEXPORT
jboolean
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_isAutoThresholdActivated
  (JNIEnv *, jclass)
{

	return tracker->isAutoThresholdActivated();
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setNumAutoThresholdRetries
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setNumAutoThresholdRetries
  (JNIEnv *, jclass, jint retries)
{
	tracker->setNumAutoThresholdRetries(retries);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setUndistortionMode
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setUndistortionMode
  (JNIEnv *, jclass, jint mode)
{
	tracker->setUndistortionMode(static_cast<UNDIST_MODE>(mode));
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setMarkerMode
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setMarkerMode
  (JNIEnv *, jclass, jint mode)
{

	tracker->setMarkerMode(static_cast<MARKER_MODE>(mode));
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    setImageProcessingMode
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_setImageProcessingMode
  (JNIEnv *, jclass, jint mode)
{
	tracker->setImageProcessingMode(static_cast<IMAGE_PROC_MODE>(mode));
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    getBitsPerPixel
 * Signature: ()I
 */
JNIEXPORT
jint
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_getBitsPerPixel
  (JNIEnv *, jclass)
{
	return tracker->getBitsPerPixel();
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    addPattern
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT
jint
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_addPattern
  (JNIEnv * env, jclass, jstring filename)
{
	const char *pattern_fname = env->GetStringUTFChars(filename, NULL);

	jint patt_id = tracker->addPattern(pattern_fname);

	env->ReleaseStringUTFChars(filename, pattern_fname);

	return patt_id;
}

/*
 * Class:     pt_com_gmv_lab_implant_jni_ARToolkitPlus
 * Method:    detectMarkers
 * Signature: ([B)[I
 */
JNIEXPORT
jintArray
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_detectMarkers
  (JNIEnv * env, jclass, jbyteArray img)
{
	int count;
	int * markers_arr;
	jintArray jmarkers_array;

	jbyte * img_matrix = env->GetByteArrayElements(img, JNI_FALSE);
	uint8_t *cam_buffer = (uint8_t*) img_matrix;

	vector<int> markers = tracker->calc(cam_buffer);

	count = markers.size();

	if(count > 0) {
		jmarkers_array = env->NewIntArray(count);
		markers_arr = new int[count];

		for(int i = 0; i < count; ++i) {
			markers_arr[i] = markers.at(i);
		}

		env->SetIntArrayRegion(jmarkers_array, 0, count, markers_arr);
	}

	cam_buffer = NULL;
	env->ReleaseByteArrayElements(img, img_matrix, 0);

	return jmarkers_array;
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    selectBestMarkerByCf
 * Signature: ()I
 */
JNIEXPORT
jint
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_selectBestMarkerByCf
  (JNIEnv *, jclass)
{
	return tracker->selectBestMarkerByCf();
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    selectDetectedMarker
 * Signature: (I)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_selectDetectedMarker
  (JNIEnv *, jclass, jint id)
{
	tracker->selectDetectedMarker(id);
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    getConfidence
 * Signature: ()F
 */
JNIEXPORT
jfloat
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_getConfidence
  (JNIEnv *, jclass)
{
	return tracker->getConfidence();
}

/*
 * Class:     pt_com_gmv_lab_implant_ar_toolkit_ARToolkitPlus
 * Method:    getModelViewMatrix
 * Signature: ()[F
 */
JNIEXPORT
jfloatArray
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_getModelViewMatrix
  (JNIEnv * env, jclass)
{
	const ARFloat* ar_matrix = const_cast<ARFloat*>(tracker->getModelViewMatrix());

	jfloatArray j_matrix = env->NewFloatArray(16); //I'm sure about this value. Check <Tracker.h>

	if(ar_matrix == NULL || j_matrix == NULL) {
			env->ThrowNew(exception_rtti, "failed to allocate model view matrix");
	}

	env->SetFloatArrayRegion(j_matrix, 0, 16, ar_matrix);

	return j_matrix;
}

/*
 * Class:     pt_com_gmv_lab_implant_jni_ARToolkitPlus
 * Method:    getProjectionMatrix
 * Signature: ()[F
 */
JNIEXPORT
jfloatArray
JNICALL
Java_pt_com_gmv_lab_implant_jni_ARToolkitPlus_getProjectionMatrix
  (JNIEnv * env, jclass)
{
	const ARFloat* ar_cmatrix = const_cast<ARFloat*>(tracker->getProjectionMatrix());

	jfloatArray j_cmatrix = env->NewFloatArray(16); //I'm ALSO sure about this value. Check <Tracker.h>

	if(ar_cmatrix == NULL || j_cmatrix == NULL) {
			env->ThrowNew(exception_rtti, "failed to allocate projection matrix");
	}

	env->SetFloatArrayRegion(j_cmatrix, 0, 16, ar_cmatrix);

	return j_cmatrix;
}

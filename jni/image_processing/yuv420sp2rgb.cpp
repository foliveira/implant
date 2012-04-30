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

#include <image_processing/yuv420sp2rgb.h>
#include <ImageProcessing.javanative.h>

#ifndef max
#define max(a,b) ({typeof(a) _a = (a); typeof(b) _b = (b); _a > _b ? _a : _b; })
#define min(a,b) ({typeof(a) _a = (a); typeof(b) _b = (b); _a < _b ? _a : _b; })
#endif

/*
 * Class:     pt_com_gmv_lab_implant_jni_ImageProcessing
 * Method:    yuv420sp2rgb
 * Signature: ([BIII[B)V
 */
JNIEXPORT
void
JNICALL
Java_pt_com_gmv_lab_implant_jni_ImageProcessing_yuv420sp2rgb
(JNIEnv* env, jobject, jbyteArray in_image, jint width, jint height, jint textureSize, jbyteArray out_image)
{
	jbyte * jyuv_image = env->GetByteArrayElements(in_image, JNI_FALSE);
	jbyte * jrgb_image = env->GetByteArrayElements(out_image, JNI_FALSE);
	
	unsigned char * yuv_image = (unsigned char *)jyuv_image;
	unsigned char * rgb_image = (unsigned char *)jrgb_image;

	color_convert_common(yuv_image, yuv_image + width * height, width, height, textureSize, rgb_image, width * height * 3, 0, 0);

	env->ReleaseByteArrayElements(in_image, jyuv_image, 0);
	env->ReleaseByteArrayElements(out_image, jrgb_image, 0);
}

/*
   YUV 4:2:0 image with a plane of 8 bit Y samples followed by an interleaved
   U/V plane containing 8 bit 2x2 subsampled chroma samples.
   except the interleave order of U and V is reversed.

                        H V
   Y Sample Period      1 1
   U (Cb) Sample Period 2 2
   V (Cr) Sample Period 2 2
 */

const int bytes_per_pixel = 2;

static inline
void
color_convert_common
(unsigned char *pY, unsigned char *pUV, int width, int height, int texture_size, unsigned char *buffer, int size, int gray, int rotate) {
	int i, j;
	int nR, nG, nB;
	int nY, nU, nV;
	int offset = 0;
	unsigned char *out = buffer;

	// YUV 4:2:0
	for (i = 0; i < height; i++) {
	    for (j = 0; j < width; j++) {
			nY = *(pY + i * width + j);
			nV = *(pUV + (i/2) * width + bytes_per_pixel * (j/2));
			nU = *(pUV + (i/2) * width + bytes_per_pixel * (j/2) + 1);
			
			// Yuv Convert
			nY -= 16;
			nU -= 128;
			nV -= 128;
			
			if (nY < 0)
				nY = 0;
			
			nB = (int)(1192 * nY + 2066 * nU);
			nG = (int)(1192 * nY - 833 * nV - 400 * nU);
			nR = (int)(1192 * nY + 1634 * nV);
			
			nR = min(262143, max(0, nR));
			nG = min(262143, max(0, nG));
			nB = min(262143, max(0, nB));
			
			nR >>= 10; nR &= 0xff;
			nG >>= 10; nG &= 0xff;
			nB >>= 10; nB &= 0xff;
			out[offset++] = (unsigned char)nR;
			out[offset++] = (unsigned char)nG;
			out[offset++] = (unsigned char)nB;
	    }
	}
}   

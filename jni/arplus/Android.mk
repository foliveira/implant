# Copyright (C) 2010 Fábio Oliveira.
#  All rights reserved.
# 
#  This file is part of the IMPLANT project.
#  
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions
#  are met:
#  1. Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#  2. Redistributions in binary form must reproduce the above copyright
#     notice, this list of conditions and the following disclaimer in the
#     documentation and/or other materials provided with the distribution.
#  3. Neither the name of the Fábio Oliveira nor the names of its contributors
#     may be used to endorse or promote products derived from this software
#     without specific prior written permission.
#  
#  THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
#  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
#  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
#  ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
#  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
#  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
#  OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
#  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
#  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
#  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
#  SUCH DAMAGE.

LOCAL_PATH := $(call my-dir)

$(info $(LOCAL_PATH))

include $(CLEAR_VARS)

LOCAL_LDLIBS 		:= -L$(SYSROOT)/usr/lib -llog
LOCAL_MODULE    	:= arplus
LOCAL_C_INCLUDES	:= $(LOCAL_PATH)/../include
LOCAL_SRC_FILES 	:= ARToolkitPlusJNI.cpp \
						Camera.cpp \
						Tracker.cpp \
						TrackerMultiMarker.cpp \
						TrackerSingleMarker.cpp \
						core/arBitFieldPattern.cpp \
						core/arDetectMarker.cpp \
						core/arDetectMarker2.cpp \
						core/arGetCode.cpp \
						core/arGetInitRot2.cpp \
						core/arGetMarkerInfo.cpp \
						core/arGetTransMat.cpp \
						core/arGetTransMat2.cpp \
						core/arGetTransMat3.cpp \
						core/arGetTransMatCont.cpp \
						core/arLabeling.cpp \
						core/arMultiActivate.cpp \
						core/arMultiGetTransMat.cpp \
						core/arMultiGetTransMatHull.cpp \
						core/arMultiReadConfigFile.cpp \
						core/arUtil.cpp \
						core/matrix.cpp \
						core/mPCA.cpp \
						core/paramDecomp.cpp \
						core/paramDistortion.cpp \
						core/rppGetTransMat.cpp \
						core/rppMultiGetTransMat.cpp \
						core/vector.cpp \
					    extra/BCH.cpp \
					    extra/Hull.cpp \
						librpp/arGetInitRot2Sub.cpp \
						librpp/librpp.cpp \
						librpp/rpp_quintic.cpp \
						librpp/rpp_svd.cpp \
						librpp/rpp_vecmat.cpp \
						librpp/rpp.cpp \

include $(BUILD_SHARED_LIBRARY)

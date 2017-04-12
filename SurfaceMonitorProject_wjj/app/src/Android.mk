LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_PACKAGE_NAME := SurfaceMonitorService
LOCAL_PRIVILEGED_MODULE := true
LOCAL_CERTIFICATE := platform
LOCAL_MULTILIB := 32
#LOCAL_PREBUILT_JNI_LIBS_arm := jni/libso/arm/libempty.so
#LOCAL_PREBUILT_JNI_LIBS_arm64 .= jni/libso/arm64/xxx.so
include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))



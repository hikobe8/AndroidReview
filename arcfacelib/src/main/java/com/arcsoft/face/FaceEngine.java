//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.arcsoft.face;

import android.content.Context;
import android.graphics.Rect;
import java.util.List;

public class FaceEngine {
    public static final int ASF_NONE = 0;
    public static final int ASF_FACE_DETECT = 1;
    public static final int ASF_FACE_RECOGNITION = 4;
    public static final int ASF_AGE = 8;
    public static final int ASF_GENDER = 16;
    public static final int ASF_FACE3DANGLE = 32;
    public static final int ASF_LIVENESS = 128;
    public static final long ASF_DETECT_MODE_VIDEO = 0L;
    public static final long ASF_DETECT_MODE_IMAGE = 4294967295L;
    public static final int CP_PAF_NV21 = 2050;
    public static final int CP_PAF_BGR24 = 513;
    public static final int ASF_OP_0_ONLY = 1;
    public static final int ASF_OP_90_ONLY = 2;
    public static final int ASF_OP_270_ONLY = 3;
    public static final int ASF_OP_180_ONLY = 4;
    public static final int ASF_OP_0_HIGHER_EXT = 5;
    public static final int ASF_OC_0 = 1;
    public static final int ASF_OC_90 = 2;
    public static final int ASF_OC_270 = 3;
    public static final int ASF_OC_180 = 4;
    public static final int ASF_OC_30 = 5;
    public static final int ASF_OC_60 = 6;
    public static final int ASF_OC_120 = 7;
    public static final int ASF_OC_150 = 8;
    public static final int ASF_OC_210 = 9;
    public static final int ASF_OC_240 = 10;
    public static final int ASF_OC_300 = 11;
    public static final int ASF_OC_330 = 12;
    private long handle = 0L;
    private ErrorInfo error = new ErrorInfo();
    private FaceInfo[] mFaceInfoArray;
    private AgeInfo[] mAgeInfoArray;
    private GenderInfo[] mGenderInfoArray;
    private Face3DAngle[] mFaceAngleArray;
    private LivenessInfo[] mLivenessInfoArray;

    private native long initFaceEngine(Context var1, long var2, int var4, int var5, int var6, int var7, ErrorInfo var8);

    private native int activeFaceEngine(Context var1, String var2, String var3);

    private native int unInitFaceEngine(long var1);

    private native int detectFaces(long var1, byte[] var3, int var4, int var5, int var6, FaceInfo[] var7, ErrorInfo var8);

    private native void process(long var1, byte[] var3, int var4, int var5, int var6, FaceInfo[] var7, int var8, ErrorInfo var9);

    private native void extractFaceFeature(long var1, byte[] var3, int var4, int var5, int var6, Rect var7, int var8, byte[] var9, ErrorInfo var10);

    private native float pairMatching(long var1, byte[] var3, byte[] var4, ErrorInfo var5);

    private native int getAge(long var1, AgeInfo[] var3, ErrorInfo var4);

    private native int getGender(long var1, GenderInfo[] var3, ErrorInfo var4);

    private native int getFace3DAngle(long var1, Face3DAngle[] var3, ErrorInfo var4);

    private native int getLiveness(long var1, LivenessInfo[] var3, ErrorInfo var4);

    private native String getVersion(long var1);

    public FaceEngine() {
    }

    public int active(Context context, String appId, String sdkKey) {
        return context != null && appId != null && sdkKey != null ? this.activeFaceEngine(context, appId, sdkKey) : 2;
    }

    public int init(Context context, long detectMode, int detectFaceOrientPriority, int detectFaceScaleVal, int detectFaceMaxNum, int combinedMask) {
        if (this.handle != 0L) {
            return 5;
        } else if (context == null) {
            return 2;
        } else {
            this.handle = this.initFaceEngine(context, detectMode, detectFaceOrientPriority, detectFaceScaleVal, detectFaceMaxNum, combinedMask, this.error);
            if (this.error.getCode() == 0) {
                int i;
                if ((combinedMask & 1) != 0) {
                    this.mFaceInfoArray = new FaceInfo[detectFaceMaxNum];

                    for(i = 0; i < detectFaceMaxNum; ++i) {
                        this.mFaceInfoArray[i] = new FaceInfo();
                    }
                }

                if ((combinedMask & 8) != 0) {
                    this.mAgeInfoArray = new AgeInfo[detectFaceMaxNum];

                    for(i = 0; i < detectFaceMaxNum; ++i) {
                        this.mAgeInfoArray[i] = new AgeInfo();
                    }
                }

                if ((combinedMask & 16) != 0) {
                    this.mGenderInfoArray = new GenderInfo[detectFaceMaxNum];

                    for(i = 0; i < detectFaceMaxNum; ++i) {
                        this.mGenderInfoArray[i] = new GenderInfo();
                    }
                }

                if ((combinedMask & 32) != 0) {
                    this.mFaceAngleArray = new Face3DAngle[detectFaceMaxNum];

                    for(i = 0; i < detectFaceMaxNum; ++i) {
                        this.mFaceAngleArray[i] = new Face3DAngle();
                    }
                }

                if ((combinedMask & 128) != 0) {
                    this.mLivenessInfoArray = new LivenessInfo[detectFaceMaxNum];

                    for(i = 0; i < detectFaceMaxNum; ++i) {
                        this.mLivenessInfoArray[i] = new LivenessInfo();
                    }
                }
            }

            return this.error.code;
        }
    }

    public int unInit() {
        if (this.handle != 0L) {
            int unInitEngineCode = this.unInitFaceEngine(this.handle);
            if (unInitEngineCode == 0) {
                this.handle = 0L;
            }

            return unInitEngineCode;
        } else {
            return 5;
        }
    }

    public int detectFaces(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList) {
        if (format != 2050 && format != 513) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null) {
                if (format == 2050 && data.length != width * height * 3 / 2 || format == 513 && data.length != width * height * 3) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    faceInfoList.clear();
                    int count = this.detectFaces(this.handle, data, width, height, format, this.mFaceInfoArray, this.error);
                    if (count > 0) {
                        for(int i = 0; i < count; ++i) {
                            faceInfoList.add(new FaceInfo(this.mFaceInfoArray[i]));
                        }
                    }

                    return this.error.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int process(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList, int combineMask) {
        if (format != 2050 && format != 513) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null) {
                if (format == 2050 && data.length != width * height * 3 / 2 || format == 513 && data.length != width * height * 3) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.process(this.handle, data, width, height, format, (FaceInfo[])faceInfoList.toArray(new FaceInfo[0]), combineMask, this.error);
                    return this.error.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int extractFaceFeature(byte[] data, int width, int height, int format, FaceInfo faceInfo, FaceFeature feature) {
        if (format != 2050 && format != 513) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (feature != null && feature.getFeatureData() != null && feature.getFeatureData().length >= 1032 && faceInfo != null && data != null) {
                if ((format != 2050 || data.length == width * height * 3 / 2) && (format != 513 || data.length == width * height * 3)) {
                    if (this.handle == 0L) {
                        return 5;
                    } else {
                        this.extractFaceFeature(this.handle, data, width, height, format, faceInfo.getRect(), faceInfo.getOrient(), feature.getFeatureData(), this.error);
                        return this.error.code;
                    }
                } else {
                    return 86021;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int compareFaceFeature(FaceFeature feature1, FaceFeature feature2, FaceSimilar faceSimilar) {
        if (feature1 != null && feature1.getFeatureData() != null && feature2 != null && feature2.getFeatureData() != null && faceSimilar != null) {
            if (this.handle == 0L) {
                return 5;
            } else {
                faceSimilar.score = this.pairMatching(this.handle, feature1.getFeatureData(), feature2.getFeatureData(), this.error);
                return this.error.code;
            }
        } else {
            return 2;
        }
    }

    public int getAge(List<AgeInfo> ageInfoList) {
        if (ageInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            ageInfoList.clear();
            int count = this.getAge(this.handle, this.mAgeInfoArray, this.error);
            if (count > 0) {
                for(int i = 0; i < count; ++i) {
                    ageInfoList.add(new AgeInfo(this.mAgeInfoArray[i]));
                }
            }

            return this.error.code;
        }
    }

    public int getGender(List<GenderInfo> genderInfoList) {
        if (genderInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            genderInfoList.clear();
            int count = this.getGender(this.handle, this.mGenderInfoArray, this.error);
            if (count > 0) {
                for(int i = 0; i < count; ++i) {
                    genderInfoList.add(new GenderInfo(this.mGenderInfoArray[i]));
                }
            }

            return this.error.code;
        }
    }

    public int getFace3DAngle(List<Face3DAngle> face3DAngleList) {
        if (face3DAngleList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            face3DAngleList.clear();
            int count = this.getFace3DAngle(this.handle, this.mFaceAngleArray, this.error);

            for(int i = 0; i < count; ++i) {
                face3DAngleList.add(new Face3DAngle(this.mFaceAngleArray[i]));
            }

            return this.error.code;
        }
    }

    public int getLiveness(List<LivenessInfo> livenessInfoList) {
        if (livenessInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            livenessInfoList.clear();
            int count = this.getLiveness(this.handle, this.mLivenessInfoArray, this.error);

            for(int i = 0; i < count; ++i) {
                livenessInfoList.add(new LivenessInfo(this.mLivenessInfoArray[i]));
            }

            return this.error.code;
        }
    }

    public int getVersion(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            versionInfo.version = this.getVersion(this.handle);
            return 0;
        }
    }
}

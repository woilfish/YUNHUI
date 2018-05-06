#include "com_pengmin_encryption_YHJniUtils.h"

/*
 * Class:     com_pengmin_encryption_YHJniUtils
 * Method:    getString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_pengmin_encryption_YHJniUtils_getString
  (JNIEnv *env, jclass obj){
   return (*env) -> NewStringUTF(env,"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKvFsCFj3abpPjBZ1kGzgM48OIHVJbrJCSAGatrhyo5Ac8YHTZpZh/i9Kdi7pa6CP7t7BoZ7zUTTorZterIRYa3THEM+g6ETp0eR4+ic2fyr7OhuenY+spYBn1Qz++8b1pJ7eeWYymH0ih0X6iMPu2SAaCL/Ns3Tm+4R3koVNTYQIDAQAB");
  }
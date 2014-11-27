#include <jni.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include "insertionsort.h"

JNIEXPORT jintArray JNICALL Java_sorter_InsertionSorter_nativesort
  (JNIEnv * env, jobject obj, jintArray inarr, jfloat prob)
{

    /* can't mutate input array "inarr", so copy it to a local, mutable
     * version: type (jint *) */
    jint * local_copy = (jint *) (*env)->GetIntArrayElements(env, inarr, NULL);

    if (NULL == local_copy) {
        printf("Could not get input array.\n");
        return NULL;
    }

    /* get array length */
    const jsize len = (*env)->GetArrayLength(env, inarr);

    /* ... mutate local_copy ... */
    local_copy[0] = 42;
    local_copy[len - 1] = 69;

    /* make a returnable int array: type jintArray */
    jintArray outarr = (*env)->NewIntArray(env, len);

    if (outarr == NULL) {
        printf("Could not create output array.\n");
        return NULL;
    }

    /* copy local array contents to returnable array */
    (*env)->SetIntArrayRegion(env, outarr, 0, len, local_copy);

    /* manage yo' memory */
    (*env)->ReleaseIntArrayElements(env, inarr, local_copy, 0);

    return outarr;
}

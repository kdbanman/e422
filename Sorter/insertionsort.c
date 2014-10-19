#include <jni.h>
#include <stdio.h>
#include "insertionsort.h"

JNIEXPORT jintArray JNICALL Java_sorter_InsertionSorter_insertionsort
  (JNIEnv * env, jobject obj, jintArray inarr)
{
    /* get incoming array as a jint array */
    jint * tosort = (jint *) (*env)->GetIntArrayElements(env, inarr, NULL);

    if (NULL == tosort) {
        printf("Could not get input array.\n");
        return NULL;
    }

    /* sort array in place with insertion sort */
    const jsize len = (*env)->GetArrayLength(env, inarr);
    int i, j, temp;

    for (i = 1; i < len; i++) {
        temp = (int) tosort[i];
        j = i - 1;
        while ((temp < (int) tosort[j]) && (j >= 0)) {
            tosort[j+1] = tosort[j];
            j--;
        }
        tosort[j+1] = (jint) temp;
    }

    /* copy back the array */
    (*env)->ReleaseIntArrayElements(env, inarr, tosort, 0);

    return tosort;
}

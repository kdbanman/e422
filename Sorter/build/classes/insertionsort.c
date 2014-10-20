#include <jni.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include "insertionsort.h"

float numberVarAccess = 0.0f;

/*
 * track number of variable accesses globally
 */
void varAcc()
{
    // increment number of variable accesses by 3 to account for this read 
    // and write, and the reason for the call
    numberVarAccess += 3.0f;
}

int didFail(float prob)
{
    float hazard = numberVarAccess * prob;

    srand(time(NULL));
    float draw = (float) rand() / (float) RAND_MAX;

    return draw >= 0.5f && draw <= 0.5f + hazard;
}

JNIEXPORT jintArray JNICALL Java_sorter_InsertionSorter_nativesort
  (JNIEnv * env, jobject obj, jintArray inarr, jfloat prob)
{

    /* get incoming array as a jint array */
    jint * tosort = (jint *) (*env)->GetIntArrayElements(env, inarr, NULL);

    if (NULL == tosort) {
        printf("Could not get input array.\n");
        return NULL;
    }

    /* sort array in place with insertion sort.
     * adapted from http://tinyurl.com/m2dv5zv */
    const jsize len = (*env)->GetArrayLength(env, inarr);
    int i, j, temp;

    varAcc(); // dereference env
    varAcc(); // read array length
    varAcc(); // create i
    varAcc(); // create j
    varAcc(); // create temp

    varAcc(); // write i
    varAcc(); // loop condition read
    varAcc(); // loop condition read
    for (i = 1; i < len; i++) {

        varAcc(); // read i
        varAcc(); // read tosort
        varAcc(); // write tmp

        temp = (int) tosort[i];

        varAcc(); // read i
        varAcc(); // write j

        j = i - 1;

        varAcc(); // read j
        varAcc(); // read tosort
        varAcc(); // read temp
        varAcc(); // read j
        while ((temp < (int) tosort[j]) && (j >= 0)) {

            varAcc(); // read j
            varAcc(); // read tosort
            varAcc(); // read j
            varAcc(); // write tosort

            tosort[j+1] = tosort[j];

            varAcc(); // read j
            varAcc(); // write j

            j--;
        }

        varAcc(); // read temp
        varAcc(); // read j
        varAcc(); // write tosort

        tosort[j+1] = (jint) temp;
    }

    /* copy tosort to a returnable jintArray */
    jintArray outarr = (*env)->NewIntArray(env, len);
    if (outarr == NULL) {
        printf("Could not create output array.\n");
        return NULL;
    }

    (*env)->SetIntArrayRegion(env, outarr, 0, len, tosort);
    (*env)->ReleaseIntArrayElements(env, inarr, tosort, 0);

    if (didFail((float) prob)) return NULL;

    return outarr;
}

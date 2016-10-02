#include <jni.h>
#include "decrypt.h"

void decrypt (long *v, long *k){
/* TEA decryption routine */
unsigned long n=32, sum, y=v[0], z=v[1];
unsigned long delta=0x9e3779b9;

	sum = delta<<5;
	while (n-- > 0){
		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		sum -= delta;
	}
	v[0] = y;
	v[1] = z;
}

JNIEXPORT jbyteArray JNICALL Java_sockio_Decryptor_decNative
  (JNIEnv * env, jobject obj, jbyteArray cipher, jbyteArray jkey)
{
    jbyte * b_cip, * b_key;
    jsize b_cip_len, b_key_len;
    long * cip, * key;
    int cip_len, key_len;

    /* get local, mutable byte array of ciphertext and key */
    b_cip = (jbyte *) (*env)->GetByteArrayElements(env, cipher, NULL);
    b_key = (jbyte *) (*env)->GetByteArrayElements(env, jkey, NULL);

    /* get array lengths. */
    b_cip_len = (*env)->GetArrayLength(env, cipher);
    b_key_len = (*env)->GetArrayLength(env, jkey);

    if (NULL == b_cip     || NULL == b_key) {
        printf("Could not get local copies of arrays in JNI.\n");
        return NULL;
    }

    if (b_cip_len % sizeof (long) != 0) {
        printf("Bad message length, %d remainder\n", b_cip_len % 8);
        return NULL;
    }

    if (b_key_len != 4 * sizeof (long)) {
        printf("Bad key length %d\n", b_key_len);
        return NULL;
    }

    cip = (long *) b_cip;
    key = (long *) b_key;

    cip_len = b_cip_len / sizeof (long);
    key_len = b_key_len / sizeof (long);

    // decrypt the long * cip
    int i;
    for (i = 0; i < cip_len - 1; i += 2) {
        decrypt(&cip[i], key);
    }

    /* make returnable byte array */
    jbyteArray out_arr = (*env)->NewByteArray(env, b_cip_len);

    /* copy encrypted message to output array */
    (*env)->SetByteArrayRegion(env, out_arr, 0, b_cip_len, b_cip);

    /* free allocated arrays */
    (*env)->ReleaseByteArrayElements(env, cipher, b_cip, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, jkey, b_key, JNI_ABORT);

    return out_arr;
}

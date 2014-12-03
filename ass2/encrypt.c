#include <jni.h>
#include "encrypt.h"

/*
 * TEA encryption algorithm.
 * This is a Feistel Network with 32 levels.
 *
 * @param v pointer to message, treated as array of 2 long elements.
 * @param k pointer to key, treated as array of 4 long elements.
 */
void encrypt (long *v, long *k)
{
        /* golden ratio delta will be added to sum in each of 32 rounds. */
        unsigned long delta = 0x9e3779b9, sum = 0, n = 32;

        /* put left half of message in y, right half in z. */
        unsigned long y = v[0], z = v[1];

	while (n-- > 0){
		sum += delta;

                /* In Feistel Network syntax:
                 *  y_n+1 = F(z_n, k)
                 *  z_n+1 = F(y_n, k)
                 *
                 * - addition used instead of XOR
                 * - key does not change
                 */
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	v[0] = y;
	v[1] = z;
}

JNIEXPORT jbyteArray JNICALL Java_sockio_Encryptor_encNative
  (JNIEnv * env, jobject obj, jbyteArray plain, jbyteArray jkey)
{
    jbyte * b_msg, * b_key;
    jsize b_msg_len, b_key_len;
    long * msg, * key;
    int msg_len, key_len;

    /* get local, mutable byte array of plaintext and key */
    b_msg = (jbyte *) (*env)->GetByteArrayElements(env, plain, NULL);
    b_key = (jbyte *) (*env)->GetByteArrayElements(env, jkey, NULL);

    /* get array lengths. */
    b_msg_len = (*env)->GetArrayLength(env, plain);
    b_key_len = (*env)->GetArrayLength(env, jkey);

    if (NULL == b_msg     || NULL == b_key
    ||  NULL == b_msg_len || NULL == b_key_len) {
        printf("Could not get local copies of arrays in JNI.\n");
        return NULL;
    }

    if (b_msg_len % sizeof (long) != 0) {
        printf("Bad message length, %d remainder\n", b_msg_len % 8);
        return NULL;
    }

    if (b_key_len != 4 * sizeof (long)) {
        printf("Bad key length %d\n", b_key_len);
        return NULL;
    }

    msg = (long *) b_msg;
    key = (long *) b_key;

    msg_len = b_msg_len / sizeof (long);
    key_len = b_key_len / sizeof (long);

    printf("DEBUG %d --> %d, %d --> %d\n", b_msg_len, msg_len, b_key_len, key_len);

    // encrypt the long * msg
    int i;
    for (i = 0; i < msg_len - 1; i += 2) {
        encrypt(&msg[i], key);
    }

    /* make returnable byte array */
    jbyteArray out_arr = (*env)->NewByteArray(env, b_msg_len);

    /* copy encrypted message to output array */
    (*env)->SetByteArrayRegion(env, out_arr, 0, b_msg_len, b_msg);

    /* free allocated arrays */
    (*env)->ReleaseByteArrayElements(env, plain, b_msg, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, jkey, b_key, JNI_ABORT);

    return out_arr;
}

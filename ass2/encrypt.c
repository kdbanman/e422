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

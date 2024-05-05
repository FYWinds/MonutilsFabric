package fyi.fyw.monutils.utils

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 *
 * RSA is one of the first public-key cryptosystems and is widely used for secure data transmission.
 * In such a cryptosystem, the encryption key is public and distinct from the decryption key which is kept secret.
 *
 * This class helps to you for in RSA encryption and decryption operation.
 *
 * @author Cafer Mert Ceyhan
 * @see <a href="https://gist.github.com/mertceyhan/010f9c224e72f5957a6162c5700219b5">Mert Ceyhan's Gist</a>
 * **/
object RSAEncryptionHelper {

    private const val RSA_ALGORITHM = "RSA"
    private const val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"

    private val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
    private val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)

    /**
     * Returns a PublicKey object from generated by String
     *
     * @param publicKeyString is PublicKey as a String
     * @throws IllegalArgumentException
     *
     * @throws java.security.InvalidKeyException
     * **/
    @OptIn(ExperimentalEncodingApi::class)
    fun getPublicKeyFromString(publicKeyString: String): PublicKey? =
        try {
            val keySpec =
                X509EncodedKeySpec(Base64.decode(publicKeyString.toByteArray()))
            keyFactory.generatePublic(keySpec)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    /**
     * Returns a PrivateKey object from generated by String
     *
     * @param privateKeyString is PrivateKey as a String
     * @throws IllegalArgumentException
     *
     * @throws java.security.InvalidKeyException
     * **/
    @OptIn(ExperimentalEncodingApi::class)
    fun getPrivateKeyFromString(privateKeyString: String): PrivateKey? =
        try {
            val keySpec =
                PKCS8EncodedKeySpec(Base64.decode(privateKeyString.toByteArray()))
            keyFactory.generatePrivate(keySpec)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    /**
     * Encrypts plain text and returns that encrypted text as String
     *
     * @param plainText is text to be encrypted
     * @param publicKey is a key for encryption
     * **/
    @OptIn(ExperimentalEncodingApi::class)
    fun encryptText(plainText: String, publicKey: PublicKey): String? =
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            Base64.encode(cipher.doFinal(plainText.toByteArray()))
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    /**
     * Decrypt encrypted text and returns that plain text as String
     *
     * @param encryptedText is a encrypted text
     * @param privateKey is a key for decryption
     *
     * @throws javax.crypto.BadPaddingException
     * **/
    @OptIn(ExperimentalEncodingApi::class)
    fun decryptText(encryptedText: String, privateKey: PrivateKey): String? =
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            String(cipher.doFinal(Base64.decode(encryptedText)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
}
package com.leaguebuddy

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import com.leaguebuddy.exceptions.EncryptionException
import com.leaguebuddy.exceptions.InvalidCipherTextException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class CryptoManager {
    private val SEPERATOR: String = "9km4FxjyCma1j~'Po!'owpl"
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getEncryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    }

    fun encrypt(plainText: String): String{
        try {
            val bytes: ByteArray = plainText.encodeToByteArray()
            val encryptCipher = getEncryptCipher()

            val encryptedBytes: String = Base64.encodeToString(encryptCipher.doFinal(bytes), Base64.DEFAULT)
            val iv: String = Base64.encodeToString(encryptCipher.iv, Base64.DEFAULT)

            return "$iv$SEPERATOR$encryptedBytes"
        } catch(e: Exception) {
            throw EncryptionException()
        }
    }

    fun decrypt(cipherText: String): String {
        try {
            val arrays = cipherText.split(SEPERATOR)
            val iv: ByteArray = Base64.decode(arrays[0], Base64.DEFAULT)
            val encryptedBytes = Base64.decode(arrays[1], Base64.DEFAULT)
            return getDecryptCipherForIv(iv).doFinal(encryptedBytes).decodeToString()
        } catch(e : Exception) {
            throw InvalidCipherTextException()
        }
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}
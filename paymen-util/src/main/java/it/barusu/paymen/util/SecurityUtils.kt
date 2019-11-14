package it.barusu.paymen.util

import org.apache.commons.codec.binary.Base64
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.ByteArrayInputStream
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

class SecurityUtils {

    companion object {
        const val DEFAULT_PROVIDER = BouncyCastleProvider.PROVIDER_NAME
        const val MD5 = "MD5"
        const val SHA1 = "SHA1"
        const val SHA1_WITH_RSA = "SHA1withRSA"
        const val SHA1PRNG = "SHA1PRNG"
        const val SHA256 = "SHA-256"
        const val SHA512 = "SHA-512"
        const val RSA = "RSA"
        const val RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1PADDING"
        const val DSA = "DSA"
        const val AES = "AES"
        const val DES = "DES"
        const val DESEDE = "DESede"
        const val PBE_WITH_SHA256_AND_256BITAES_CBC_BC = "PBEWithSHA256And256BitAES-CBC-BC"
        const val SM2 = "SM2"
        const val SM3 = "SM3"

        const val X509 = "X.509"
        const val PKCS12 = "PKCS12"

        const val ENCRYPT_BLOCK_KEY_SIZE = 117
        const val DECRYPT_BLOCK_KEY_SIZE = 128

        init {
            Security.addProvider(BouncyCastleProvider())
        }

        @JvmStatic
        fun digest(algorithm: String, data: ByteArray, salt: ByteArray? = null): ByteArray {
            val digest = MessageDigest.getInstance(algorithm, DEFAULT_PROVIDER)
            if (salt != null) {
                digest.update(salt)
            }
            digest.update(data)
            return digest.digest()
        }

        @JvmStatic
        fun getKeyStore(type: String, content: String, password: String,
                        provider: String? = DEFAULT_PROVIDER): KeyStore {
            require(type.isNotEmpty())
            require(Base64.isBase64(content)) { "The content of key must be encoded as base64." }

            val keyStore = if (provider.isNullOrEmpty())
                KeyStore.getInstance(type) else KeyStore.getInstance(type, provider)
            keyStore.load(ByteArrayInputStream(Base64.decodeBase64(content)), password.toCharArray())
            return keyStore
        }


        @JvmStatic
        fun getPrivateKey(type: String, content: String, password: String? = null,
                          provider: String? = DEFAULT_PROVIDER): PrivateKey {
            require(type.isNotEmpty())
            require(Base64.isBase64(content)) { "The content of private key must be encoded as base64." }

            if (password.isNullOrEmpty()) {
                val keyFactory = if (provider.isNullOrEmpty())
                    KeyFactory.getInstance(type) else KeyFactory.getInstance(type, provider)
                return keyFactory.generatePrivate(PKCS8EncodedKeySpec(Base64.decodeBase64(content)))
            } else {
                val keyStore = getKeyStore(type, content, password, provider)
                val keyAliases = keyStore.aliases()
                var privateKey: PrivateKey? = null

                while (keyAliases.hasMoreElements()) {
                    val keyAlias: String = keyAliases.nextElement()
                    if (keyStore.isKeyEntry(keyAlias)) {
                        privateKey = keyStore.getKey(keyAlias, password.toCharArray()) as PrivateKey
                        break
                    }
                }

                if (privateKey == null) {
                    throw InvalidKeyException("No avaliable private key was found.")
                }
                return privateKey
            }
        }

        @JvmStatic
        fun getCertificate(type: String, content: String, provider: String? = DEFAULT_PROVIDER): Certificate {
            val certificateFactory = if (provider.isNullOrEmpty())
                CertificateFactory.getInstance(type) else CertificateFactory.getInstance(type, provider)
            return certificateFactory.generateCertificate(ByteArrayInputStream(Base64.decodeBase64(content)))
        }

        /**
         * get certificate from keystore.
         * @see SecurityUtils.getKeyStore
         */
        @JvmStatic
        fun getCertificate(keyStore: KeyStore): X509Certificate {
            val keyAliases = keyStore.aliases()
            if (!keyAliases.hasMoreElements()) {
                throw SecurityException("Error generating X509Certificate from the private key!")
            }
            val keyAlias = keyAliases.nextElement()
            return keyStore.getCertificate(keyAlias) as X509Certificate
        }

        @JvmStatic
        fun getPublicKey(type: String, content: String, provider: String? = DEFAULT_PROVIDER): PublicKey {
            require(type.isNotEmpty())
            require(Base64.isBase64(content)) { "The content of public key must be encoded as base64." }

            return if (X509 == type) {
                getCertificate(type, content, provider).publicKey
            } else {
                val keyFactory = if (provider.isNullOrEmpty())
                    KeyFactory.getInstance(type) else KeyFactory.getInstance(type, provider)
                keyFactory.generatePublic(X509EncodedKeySpec(Base64.decodeBase64(content)))
            }
        }

        @JvmStatic
        fun generateSalt(algorithm: String): ByteArray {
            val salt = ByteArray(8)
            SecureRandom.getInstance(algorithm).nextBytes(salt)

            return salt
        }

        @JvmStatic
        fun getSecretKey(algorithm: String, provider: String? = DEFAULT_PROVIDER): SecretKey {
            return if (provider.isNullOrEmpty()) KeyGenerator.getInstance(algorithm).generateKey()
            else KeyGenerator.getInstance(algorithm, provider).generateKey()
        }


        @JvmStatic
        fun getSecretKey(algorithm: String, spec: KeySpec, provider: String? = DEFAULT_PROVIDER): SecretKey {
            return if (provider.isNullOrEmpty()) SecretKeyFactory.getInstance(algorithm).generateSecret(spec)
            else SecretKeyFactory.getInstance(algorithm, provider).generateSecret(spec)
        }

        @JvmStatic
        fun sign(algorithm: String, privateKey: PrivateKey, data: ByteArray,
                 provider: String? = DEFAULT_PROVIDER): ByteArray {
            val signature = if (provider.isNullOrEmpty())
                Signature.getInstance(algorithm) else Signature.getInstance(algorithm, provider)
            signature.initSign(privateKey)
            signature.update(data)
            return signature.sign()
        }

        @JvmStatic
        fun verify(algorithm: String, publicKey: PublicKey,
                   data: ByteArray, signByteArray: ByteArray, provider: String? = DEFAULT_PROVIDER): Boolean {
            val signature = if (provider.isNullOrEmpty())
                Signature.getInstance(algorithm) else Signature.getInstance(algorithm, provider)
            signature.initVerify(publicKey)
            signature.update(data)
            return signature.verify(signByteArray)
        }

        @JvmStatic
        fun encrypt(algorithm: String, key: Key, data: ByteArray, spec: AlgorithmParameterSpec? = null,
                    provider: String? = DEFAULT_PROVIDER): ByteArray {
            val cipher = if (provider.isNullOrEmpty())
                Cipher.getInstance(algorithm) else Cipher.getInstance(algorithm, provider)
            if (spec == null) {
                cipher.init(Cipher.ENCRYPT_MODE, key)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, spec)
            }

            return cipher.doFinal(data)
        }

        @JvmStatic
        fun blockEncrypt(algorithm: String, key: Key, data: ByteArray,
                         encryptBlockKeySize: Int = ENCRYPT_BLOCK_KEY_SIZE,
                         provider: String? = DEFAULT_PROVIDER): ByteArray {
            val cipher = if (provider.isNullOrEmpty())
                Cipher.getInstance(algorithm) else Cipher.getInstance(algorithm, provider)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            var result = ByteArray(0)
            var i = 0
            while (i < data.size) {
                val j = if (i + encryptBlockKeySize > data.size) data.size else i + encryptBlockKeySize
                val finalByteArray = cipher.doFinal(data.copyOfRange(i, j))
                val newArray = ByteArray(result.size + finalByteArray.size)
                result.copyInto(newArray, 0)
                finalByteArray.copyInto(newArray, result.size)
                result = newArray
                i += encryptBlockKeySize
            }
            return result
        }

        @JvmStatic
        fun decrypt(algorithm: String, key: Key, data: ByteArray, spec: AlgorithmParameterSpec? = null,
                    provider: String? = DEFAULT_PROVIDER): ByteArray {
            val cipher = if (provider.isNullOrEmpty())
                Cipher.getInstance(algorithm) else Cipher.getInstance(algorithm, provider)
            if (spec == null) {
                cipher.init(Cipher.DECRYPT_MODE, key)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, spec)
            }

            return cipher.doFinal(data)
        }

        @JvmStatic
        fun blockDecrypt(algorithm: String, key: Key, data: ByteArray,
                         decryptBlockKeySize: Int = DECRYPT_BLOCK_KEY_SIZE,
                         provider: String? = DEFAULT_PROVIDER): ByteArray {
            val cipher = if (provider.isNullOrEmpty())
                Cipher.getInstance(algorithm) else Cipher.getInstance(algorithm, provider)
            cipher.init(Cipher.DECRYPT_MODE, key)
            var result = ByteArray(0)
            var i = 0
            while (i < data.size) {
                val j = if (i + decryptBlockKeySize > data.size) data.size else i + decryptBlockKeySize
                val finalByteArray = cipher.doFinal(data.copyOfRange(i, j))
                val newArray = ByteArray(result.size + finalByteArray.size)
                result.copyInto(newArray, 0)
                finalByteArray.copyInto(newArray, result.size)
                result = newArray
                i += decryptBlockKeySize
            }
            return result
        }


    }
}